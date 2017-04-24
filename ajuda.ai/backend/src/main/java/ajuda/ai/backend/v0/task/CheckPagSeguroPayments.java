package ajuda.ai.backend.v0.task;
//package ajuda.ai.website.task;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.Map;
//
//import javax.ejb.Schedule;
//import javax.inject.Inject;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
//import javax.transaction.Transactional;
//
//import org.hibernate.annotations.QueryHints;
//import org.slf4j.Logger;
//
//import ajuda.ai.model.billing.Payment;
//import ajuda.ai.model.billing.PaymentEvent;
//import ajuda.ai.model.billing.PaymentServiceEnum;
//import ajuda.ai.util.JsonUtils;
//import br.com.uol.pagseguro.domain.AccountCredentials;
//import br.com.uol.pagseguro.domain.Transaction;
//import br.com.uol.pagseguro.service.NotificationService;
//
///**
// * Checa a cada 4 horas pelo status das compras do PagSeguro que ainda não foram concluídas ou canceladas.
// *
// * @author Rafael Lins
// *
// */
//public class CheckPagSeguroPayments {
//	private static final BigDecimal HUNDRED = new BigDecimal(100);
//
//	@Inject
//	private Logger log;
//
//	@PersistenceContext
//	private EntityManager entityManager;
//
//	@Transactional
//	@Schedule(hour="4,12,20")
//	public void run() {
//		log.info("Inicando sincronização de pagamentos com PagSeguro...");
//		final long start = System.currentTimeMillis();
//
//		final Query query = entityManager.createQuery("FROM Payment WHERE paymentService = :paymentService AND cancelled = false AND readyForAccounting = false").setParameter("paymentService", PaymentServiceEnum.PAG_SEGURO);
//		query.setHint(QueryHints.FETCH_SIZE, 100);	// Pega de 100 em 100 resultados de forma transparente
//
//		final List<Payment> payments = query.getResultList();
//		for (final Payment payment : payments) {
//			try {
//				final Map<String, String> credentials = JsonUtils.fromJson(payment.getInstitution().getPaymentServiceData(), Map.class);
//				final AccountCredentials accountCred = new AccountCredentials(credentials.get("email"), credentials.get("token"));
//				final Transaction transaction = NotificationService.checkTransaction(accountCred, payment.getPaymentServiceId());
//				final PaymentEvent lastEvent = payment.getEvents().get(0);
//
//				if (lastEvent.getStatus() != transaction.getStatus().getValue()) {
//					final PaymentEvent newEvent = new PaymentEvent();
//					newEvent.setCurrency("BRL");	// Apenas BRL é suportado no momento (pelo PagSeguro)
//					newEvent.setPayment(payment);
//					newEvent.setPaymentType(transaction.getPaymentMethod().getCode().getValue().toString());
//					newEvent.setPaymentTypeInfo(transaction.getPaymentMethod().getType().getType());
//					newEvent.setStatus(transaction.getStatus().getValue());
//					newEvent.setTimestamp(transaction.getLastEventDate());
//					newEvent.setTransactionServiceId(transaction.getCode());
//					newEvent.setValue(transaction.getGrossAmount().multiply(HUNDRED).intValue());
//
//					entityManager.persist(newEvent);
//
//					final Query updatePayment = entityManager.createQuery("UPDATE Payment SET paid = :paid, cancelled = :cancelled, readyForAccounting = :readyForAccounting WHERE id = :id").setParameter("id", payment.getId());
//					updatePayment.setParameter("paid", PaymentServiceEnum.PAG_SEGURO.isPaid(newEvent.getStatus()));
//					updatePayment.setParameter("cancelled", PaymentServiceEnum.PAG_SEGURO.isCancelled(newEvent.getStatus()));
//					updatePayment.setParameter("readyForAccounting", PaymentServiceEnum.PAG_SEGURO.isReadyForAccounting(newEvent.getStatus()));
//					updatePayment.executeUpdate();
//				}
//			} catch (final Exception e) {
//				log.error("Erro ao processar pagamento ID #" + payment.getId(), e);
//			}
//		}
//
//		log.info("Pagamentos do PagSeguro processados em {} segundos", (System.currentTimeMillis() - start) / 1000.0);
//	}
//}
