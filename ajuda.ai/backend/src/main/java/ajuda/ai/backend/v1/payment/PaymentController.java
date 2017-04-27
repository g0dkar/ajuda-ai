package ajuda.ai.backend.v1.payment;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;

import ajuda.ai.model.billing.Payment;
import ajuda.ai.payment.PaymentGateway;
import ajuda.ai.payment.PaymentService;
import ajuda.ai.payment.exception.UnsupportedPaymentServiceException;
import ajuda.ai.persistence.model.billing.PaymentPersistence;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;

/**
 * Redireciona o usuário para efetuar um determinado pagamento
 * @author Rafael Lins
 *
 */
@Controller
@Path("/pagar")
public class PaymentController {
	private final Logger log;
	private final Result result;
	private final PaymentService paymentService;
	private final PaymentPersistence pp;
	
	/** @deprecated CDI */ @Deprecated
	PaymentController() { this(null, null, null, null); }
	
	@Inject
	public PaymentController(final Logger log, final Result result, final PaymentService paymentService, final PaymentPersistence pp) {
		this.log = log;
		this.result = result;
		this.paymentService = paymentService;
		this.pp = pp;
	}
	
	@Transactional
	@Get("/{uuid:[a-f0-9]+}")
	public void redirectToPayment(final String uuid) {
		if (log.isDebugEnabled()) {
			log.debug("Buscando Payment: {}", uuid);
		}
		
		final Payment payment = pp.get(uuid);
		
		if (log.isDebugEnabled()) {
			log.debug("Payment encontrado: {}", payment);
		}
		
		if (payment != null) {
			if (!payment.isCancelled() && !payment.isPaid()) {
				try {
					final PaymentGateway gateway = paymentService.get(payment.getPaymentService());
					gateway.redirectToPayment(payment, result);
				} catch (final UnsupportedPaymentServiceException upse) {
					log.error("Gateway de pagamento não suportado!", upse);
					result.notFound();
				}
			}
			else {
				result.notFound();
			}
		}
		else {
			result.notFound();
		}
	}
}
