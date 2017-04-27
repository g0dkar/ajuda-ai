package ajuda.ai.payment;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import ajuda.ai.payment.annotation.PaymentServiceImpl;
import ajuda.ai.payment.exception.UnsupportedPaymentServiceException;
import ajuda.ai.util.StringUtils;

/**
 * Mantém um registro dos serviços de pagamento e instancia as implementações.
 * 
 * @author Rafael Lins
 *
 */
@RequestScoped
public class PaymentService {
	/**
	 * Todas as instâncias que podem ser criadas pelo CDI serão injetadas neste ponto
	 */
	@Inject
	private Instance<PaymentGateway> processors;
	
	/**
	 * @param service Serviço de pagamento
	 * @return Um {@link PaymentGateway} do serviço especificado
	 */
	public PaymentGateway get(final String service) {
		if (!StringUtils.isBlank(service)) {
			// Percorremos todas as instâncias
			for (final PaymentGateway paymentProcessor : processors) {
				// E capturamos a annotation, se presente
				PaymentServiceImpl annotation = paymentProcessor.getClass().getAnnotation(PaymentServiceImpl.class);
				
				// Esse segundo passo é necessário por algumas implementações CDI (em particular o Weld) usam proxies, os quais não tem as annotations da classe original
				// É considerado um bug e, no Weld, ainda está como "Não Resolvido" (2017-04-26)
				// Isso funciona pois proxies (geralmente) têm como superclasse a classe original
				if (annotation == null) {
					annotation = paymentProcessor.getClass().getSuperclass().getAnnotation(PaymentServiceImpl.class);
				}
				
				// Verificamos a annotation e...
				if (annotation != null && annotation.value().equals(service)) {
					// Voilá!
					return paymentProcessor;
				}
			}
		}
		
		// Se nada for encontrado, esse serviço não é supotado
		throw new UnsupportedPaymentServiceException(service);
	}
}
