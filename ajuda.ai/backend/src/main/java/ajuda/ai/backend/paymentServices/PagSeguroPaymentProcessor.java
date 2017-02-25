package ajuda.ai.backend.paymentServices;
//package ajuda.ai.website.paymentServices;
//
//import java.math.BigDecimal;
//import java.util.Date;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import javax.persistence.Query;
//import javax.servlet.http.HttpServletRequest;
//
//import org.jsoup.helper.StringUtil;
//import org.slf4j.Logger;
//
//import ajuda.ai.model.billing.Payment;
//import ajuda.ai.model.billing.PaymentEvent;
//import ajuda.ai.model.billing.PaymentServiceEnum;
//import ajuda.ai.model.institution.Institution;
//import ajuda.ai.model.institution.Helper;
//import ajuda.ai.util.JsonUtils;
//import ajuda.ai.website.util.PersistenceService;
//import br.com.caelum.vraptor.Result;
//import br.com.uol.pagseguro.domain.AccountCredentials;
//import br.com.uol.pagseguro.domain.Transaction;
//import br.com.uol.pagseguro.domain.checkout.Checkout;
//import br.com.uol.pagseguro.enums.Currency;
//import br.com.uol.pagseguro.exception.PagSeguroServiceException;
//import br.com.uol.pagseguro.properties.PagSeguroConfig;
//import br.com.uol.pagseguro.service.NotificationService;
//
///**
// * Cria uma ordem de pagamento junto ao PagSeguro
// *
// * @author Rafael Lins
// *
// */
//public class PagSeguroPaymentProcessor implements PaymentProcessor {
//	public Payment createPayment(final Institution institution, final Helper institutionHelper, final int value, final PersistenceService ps, final Result result, final Logger log) {
//		final String paymentDescription = "Doação: " + institution.getName();
//		final BigDecimal valueBigDecimal = new BigDecimal(value).divide(HUNDRED).setScale(2);
//
//		final Checkout checkout = new Checkout();
//		checkout.addItem(institution.getId().toString(), paymentDescription, 1, valueBigDecimal, null, null);
//		checkout.setCurrency(Currency.BRL);
//		checkout.setReference(institution.getId() + "-" + institutionHelper.getId());
//		checkout.setRedirectURL("https://ajuda.ai/" + institution.getSlug() + "/obrigado");
//		checkout.setNotificationURL("https://ajuda.ai/" + institution.getSlug() + "/api/transaction-notification");
//
//		try {
//			final boolean onlyCheckoutCode = false;
//			final String response = checkout.register(PagSeguroConfig.getAccountCredentials(), onlyCheckoutCode);
//			final Matcher codeMatcher = Pattern.compile("code=([A-F0-9]+)").matcher(response);
//			final String pagSeguroId = codeMatcher.find() ? codeMatcher.group(1) : null;
//
//			final Payment payment = new Payment();
//			payment.setInstitution(institution);
//			payment.setHelper(institutionHelper);
//			payment.setPaymentServiceId(pagSeguroId);
//			payment.setDescription("Pagamento PagSeguro");
//			payment.setPaymentService(institution.getPaymentService());
//			payment.setTimestamp(new Date());
//			payment.setValue(value);
//			payment.setPaid(false);
//			payment.setCancelled(false);
//			payment.setReadyForAccounting(false);
//			ps.persist(payment);
//
//			result.redirectTo(response);
//			return payment;
//		} catch (final PagSeguroServiceException e) {
//			log.error("Erro na comunicação com o PagSeguro", e);
//		}
//
//		return null;
//	}
//
//	public PaymentEvent processEvent(final Institution institution, final HttpServletRequest request, final PersistenceService ps, final Result result, final Logger log) throws Exception {
//		if (log.isDebugEnabled()) { log.info("Iniciando Processamento de Pagamento do PagSeguro"); }
//		if (request.getMethod().equals("POST")) {
//			final String pagSeguroId = request.getParameter("notificationCode");
//			final Map<String, String> credentials = JsonUtils.fromJson(institution.getPaymentServiceData(), Map.class);
//			final AccountCredentials accountCred = new AccountCredentials(credentials.get("email"), credentials.get("token"), credentials.get("token"));
//			if (log.isDebugEnabled()) { log.info("Pedindo ao PagSeguro informações sobre a transação"); }
//			final Transaction transaction = NotificationService.checkTransaction(accountCred, pagSeguroId);
//			final int valor = transaction.getGrossAmount().multiply(HUNDRED).intValue();
//
//			Helper institutionHelper = (Helper) ps.createQuery("FROM Helper WHERE institution = :institution AND LOWER(email) = LOWER(:email)").setParameter("institution", institution).setParameter("email", transaction.getSender().getEmail()).getSingleResult();
//			if (institutionHelper == null) {
//				if (log.isDebugEnabled()) { log.info("Pagamento veio de um Helper inexistente, criando..."); }
//				institutionHelper = new Helper();
//				institutionHelper.setAllowPublish(false);
//				institutionHelper.setEmail(transaction.getSender().getEmail());
//				institutionHelper.setPaymentEmail(transaction.getSender().getEmail());
//				institutionHelper.setName(transaction.getSender().getName());
//				institutionHelper.setInstitution(institution);
//				institutionHelper.setPhone(transaction.getSender().getPhone().toString());
//				ps.persist(institutionHelper);
//			}
//			else if (StringUtil.isBlank(institutionHelper.getPhone())) {
//				if (log.isDebugEnabled()) { log.info("Pagamento veio de um Helper que já existe. Atualizando PaymentEmail e Phone..."); }
//				institutionHelper.setPaymentEmail(transaction.getSender().getEmail());
//				institutionHelper.setPhone(transaction.getSender().getPhone().toString());
//				ps.merge(institutionHelper);
//			}
//
//			Payment payment = (Payment) ps.createQuery("FROM Payment WHERE institution = :institution AND paymentServiceId = :id").setParameter("institution", institution).setParameter("id", pagSeguroId).getSingleResult();
//
//			if (payment == null) {
//				if (log.isDebugEnabled()) { log.info("Pagamento referente a um Payment não registrado. Criando..."); }
//				payment = new Payment();
//				payment.setInstitution(institution);
//				payment.setHelper(institutionHelper);
//				payment.setDescription("Pagamento criado via notificação de pagamento do PagSeguro");
//				payment.setPaymentService(institution.getPaymentService());
//				payment.setPaymentServiceId(transaction.getCode());
//				payment.setTimestamp(new Date());
//				payment.setValue(valor);
//				payment.setPaid(false);
//				payment.setCancelled(false);
//				payment.setReadyForAccounting(false);
//				ps.persist(payment);
//			}
//			else {
//				payment.setPaymentServiceId(transaction.getCode());
//				ps.merge(payment);
//			}
//
//			ps.createQuery("UPDATE Helper SET lastPayment = :payment WHERE id = :id").setParameter("payment", payment).setParameter("id", institutionHelper.getId()).executeUpdate();
//
//			final PaymentEvent newEvent = new PaymentEvent();
//			newEvent.setCurrency("BRL");	// Apenas BRL é suportado no momento (pelo PagSeguro)
//			newEvent.setPayment(payment);
//			newEvent.setPaymentType(transaction.getPaymentMethod().getCode().getValue().toString());
//			newEvent.setPaymentTypeInfo(transaction.getPaymentMethod().getType().getType());
//			newEvent.setStatus(transaction.getStatus().getValue());
//			newEvent.setTimestamp(transaction.getLastEventDate());
//			newEvent.setTransactionServiceId(transaction.getCode());
//			newEvent.setValue(valor);
//			ps.persist(newEvent);
//
//			final Query updatePayment = ps.createQuery("UPDATE Payment SET paid = :paid, cancelled = :cancelled, readyForAccounting = :readyForAccounting WHERE id = :id").setParameter("id", payment.getId());
//			updatePayment.setParameter("paid", PaymentServiceEnum.PAG_SEGURO.isPaid(newEvent.getStatus()));
//			updatePayment.setParameter("cancelled", PaymentServiceEnum.PAG_SEGURO.isCancelled(newEvent.getStatus()));
//			updatePayment.setParameter("readyForAccounting", PaymentServiceEnum.PAG_SEGURO.isReadyForAccounting(newEvent.getStatus()));
//			updatePayment.executeUpdate();
//
//			result.nothing();
//			return newEvent;
//		}
//		else if (log.isDebugEnabled()) { log.debug("Ignorando notificação de pagamento do PagSeguro: Request não é POST"); }
//
//		return null;
//	}
//
//}
