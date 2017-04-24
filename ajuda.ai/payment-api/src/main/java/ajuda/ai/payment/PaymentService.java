package ajuda.ai.payment;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import ajuda.ai.payment.exception.UnsupportedPaymentServiceException;

/**
 * Mantém um registro dos serviços de pagamento e instancia as implementações.
 * 
 * @author Rafael Lins
 *
 */
@ApplicationScoped
public class PaymentService {
	@Inject @ajuda.ai.payment.annotation.PaymentService
	private Instance<PaymentGateway> processors;
	
	/**
	 * @param service Serviço de pagamento
	 * @return Um {@link PaymentGateway} do serviço especificado
	 */
	public PaymentGateway get(String service) {
		for (PaymentGateway paymentProcessor : processors) {
			ajuda.ai.payment.annotation.PaymentService annotation = paymentProcessor.getClass().getAnnotation(ajuda.ai.payment.annotation.PaymentService.class);
			
			if (annotation.value().equals(service)) {
				return paymentProcessor;
			}
		}
		
		throw new UnsupportedPaymentServiceException(service);
	}
}
