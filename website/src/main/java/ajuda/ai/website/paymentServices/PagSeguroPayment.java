package ajuda.ai.website.paymentServices;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import ajuda.ai.model.billing.Payment;
import ajuda.ai.model.billing.PaymentEvent;
import ajuda.ai.model.institution.Institution;
import ajuda.ai.model.institution.InstitutionHelper;
import ajuda.ai.util.JsonUtils;
import ajuda.ai.website.util.PersistenceService;
import br.com.caelum.vraptor.Result;
import br.com.uol.pagseguro.domain.AccountCredentials;
import br.com.uol.pagseguro.domain.Phone;
import br.com.uol.pagseguro.domain.Sender;
import br.com.uol.pagseguro.domain.Transaction;
import br.com.uol.pagseguro.domain.paymentrequest.PaymentRequest;
import br.com.uol.pagseguro.domain.paymentrequest.PaymentRequestItem;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.service.NotificationService;

/**
 * Cria uma ordem de pagamento junto ao PagSeguro
 * 
 * @author Rafael Lins
 *
 */
public class PagSeguroPayment implements PaymentProcessor {
	private static final Pattern PHONE_PATTERN = Pattern.compile("\\(?(\\d{2})\\)?\\s*(\\d{4,5})\\s*-\\s*(\\d{4})");
	
	public Payment createPayment(final Institution institution, final InstitutionHelper institutionHelper, final int value, final String name, final String email, final String phone) {
		final String paymentDescription = "Doação: " + institution.getName();
		final Phone phoneObj;
		
		final Matcher matcher = PHONE_PATTERN.matcher(phone);
		if (matcher.matches()) {
			phoneObj = new Phone(matcher.group(1), matcher.group(2) + matcher.group(3));
		}
		else {
			phoneObj = null;
		}
		
		final Sender sender = new Sender(name, email, phoneObj);
		
		final PaymentRequestItem item = new PaymentRequestItem(null, "Doação: " + institution.getName(), new BigDecimal(value).divide(PaymentProcessor.HUNDRED, 2), 1);
		final List<PaymentRequestItem> items = new ArrayList<PaymentRequestItem>(1);
		items.add(item);
		
		final PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setSender(sender);
		paymentRequest.setName(paymentDescription);
		paymentRequest.setDescription("Doação");
		paymentRequest.setItems(items);
		paymentRequest.setExpiration(30);
		
		try {
			final Map<String, String> credentials = JsonUtils.fromJson(institution.getPaymentServiceData(), Map.class);
			final AccountCredentials accountCred = new AccountCredentials(credentials.get("email"), credentials.get("token"));
			final String paymentRequestCode = paymentRequest.register(accountCred);
			
			final Payment payment = new Payment();
			payment.setInstitution(institution);
			payment.setInstitutionHelper(institutionHelper);
			payment.setPaymentServiceId(paymentRequestCode);
			payment.setDescription(paymentDescription);
			payment.setPaymentService(institution.getPaymentService());
			payment.setTimestamp(new Date());
			payment.setValue(value);
			payment.setPaid(false);
			payment.setCancelled(false);
			payment.setReadyForAccounting(false);
			
			return payment;
		} catch (final PagSeguroServiceException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public PaymentEvent processEvent(final Institution institution, final HttpServletRequest request, final PersistenceService ps, final Result result) throws Exception {
		if (request.getMethod().equals("POST")) {
			final String notificationType = request.getParameter("notificationType");
			if (notificationType == null || notificationType.equalsIgnoreCase("transaction")) {
				final String pagSeguroId = request.getParameter("notificationCode");
				final Payment payment = (Payment) ps.createQuery("FROM Payment WHERE institution = :institution AND paymentServiceId = :id").setParameter("institution", institution).setParameter("id", pagSeguroId).getSingleResult();
				
				if (payment != null) {
					final Map<String, String> credentials = JsonUtils.fromJson(payment.getInstitution().getPaymentServiceData(), Map.class);
					final AccountCredentials accountCred = new AccountCredentials(credentials.get("email"), credentials.get("token"));
					final Transaction transaction = NotificationService.checkTransaction(accountCred, payment.getPaymentServiceId());
					final PaymentEvent lastEvent = payment.getEvents().get(0);
					
					if (lastEvent.getStatus() != transaction.getStatus().getValue()) {
						final PaymentEvent newEvent = new PaymentEvent();
						newEvent.setCurrency("BRL");	// Apenas BRL é suportado no momento (pelo PagSeguro)
						newEvent.setPayment(payment);
						newEvent.setPaymentType(transaction.getPaymentMethod().getCode().getValue().toString());
						newEvent.setPaymentTypeInfo(transaction.getPaymentMethod().getType().getType());
						newEvent.setStatus(transaction.getStatus().getValue());
						newEvent.setTimestamp(transaction.getLastEventDate());
						newEvent.setTransactionServiceId(transaction.getCode());
						newEvent.setValue(transaction.getGrossAmount().multiply(HUNDRED).intValue());
						
						result.nothing();
						return newEvent;
					}
				}
			}
		}
		
		return null;
	}
	
}
