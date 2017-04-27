package ajuda.ai.backend.v1.payment;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.slf4j.Logger;

import ajuda.ai.payment.PaymentGateway;
import ajuda.ai.payment.PaymentService;
import ajuda.ai.payment.exception.UnsupportedPaymentServiceException;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;

@Controller
@Path("/transaction")
public class TransactionNotificationController {
	private final Logger log;
	private final Result result;
	private final PaymentService paymentService;
	private final HttpServletRequest request;
	
	/** @deprecated CDI */ @Deprecated
	TransactionNotificationController() { this(null, null, null, null); }
	
	@Inject
	public TransactionNotificationController(final Logger log, final HttpServletRequest request, final Result result, final PaymentService paymentService) {
		this.log = log;
		this.result = result;
		this.paymentService = paymentService;
		this.request = request;
	}
	
	@Transactional
	@Path("/{slug:[a-z0-9]+}")
	public void process(final String service) {
		try {
			final PaymentGateway gateway = paymentService.get(service);
			gateway.processEvent(request, result);
		} catch (final UnsupportedPaymentServiceException upse) {
			log.error("Notificação de pagamento de serviço desconhecido", upse);
			result.notFound();
		} catch (final Exception e) {
			log.error("Erro processando notificação de pagamento do serviço " + service, e);
		}
	}
}
