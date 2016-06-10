package ajuda.ai.website.paymentServices;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ajuda.ai.model.billing.Payment;
import ajuda.ai.model.institution.Institution;
import ajuda.ai.model.institution.InstitutionHelper;
import ajuda.ai.util.JsonUtils;
import br.com.uol.pagseguro.domain.AccountCredentials;
import br.com.uol.pagseguro.domain.Phone;
import br.com.uol.pagseguro.domain.Sender;
import br.com.uol.pagseguro.domain.paymentrequest.PaymentRequest;
import br.com.uol.pagseguro.domain.paymentrequest.PaymentRequestItem;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;

/**
 * Cria uma ordem de pagamento junto ao PagSeguro
 * 
 * @author Rafael Lins
 *
 */
public class PagSeguroPayment implements PaymentService {
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
		
		final PaymentRequestItem item = new PaymentRequestItem(null, "Doação: " + institution.getName(), new BigDecimal(value).divide(PaymentService.HUNDRED, 2), 1);
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
	
}
