package ajuda.ai.website;

import javax.inject.Inject;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.slf4j.Logger;

import ajuda.ai.model.billing.PaymentEvent;
import ajuda.ai.model.billing.PaymentServiceEnum;
import ajuda.ai.model.institution.Institution;
import ajuda.ai.website.paymentServices.PaymentProcessor;
import ajuda.ai.website.paymentServices.PaymentService;
import ajuda.ai.website.util.PersistenceService;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Controller
public class TransactionNotificationsController {
	private final Logger log;
	private final Result result;
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private final PersistenceService ps;
	private final PaymentService paymentService;
	
	/** @deprecated CDI */ @Deprecated
	TransactionNotificationsController() { this(null, null, null, null, null, null); }
	
	@Inject
	public TransactionNotificationsController(final Logger log, final Result result, final HttpServletRequest request, final HttpServletResponse response, final PersistenceService ps, final PaymentService paymentService) {
		this.log = log;
		this.result = result;
		this.request = request;
		this.response = response;
		this.ps = ps;
		this.paymentService = paymentService;
	}
	
	@Transactional
	@Path("/{slug:[a-z][a-z0-9\\-]*[a-z0-9]}/api/transaction-notification")
	public void notification(final String slug) {
		log.info("Recebida uma notificação de transação! Slug = /{}, request = ", slug, request);
		if (log.isDebugEnabled()) { log.debug("Processando notificação de transação"); }
		
//		response.addHeader("Access-Control-Allow-Origin", "*");
		
		final Institution institution = (Institution) ps.createQuery("FROM Institution WHERE slug = :slug").setParameter("slug", slug).getSingleResult();
		
		if (institution != null) {
			if (log.isDebugEnabled()) { log.debug("Transação para Instituição: {} (/{}, #{})", institution.getName(), institution.getSlug(), institution.getId()); }
			
			final PaymentProcessor paymentProcessor = paymentService.get(institution.getPaymentService());
			if (log.isDebugEnabled()) { log.debug("PaymentProcessor: {}", paymentProcessor); }
			
			if (paymentProcessor != null) {
				try {
					final PaymentEvent newEvent = paymentProcessor.processEvent(institution, request, ps, result);
					
					if (newEvent != null) {
						ps.persist(newEvent);
						
						final Query updatePayment = ps.createQuery("UPDATE Payment SET paid = :paid, cancelled = :cancelled, readyForAccounting = :readyForAccounting WHERE id = :id").setParameter("id", newEvent.getPayment().getId());
						updatePayment.setParameter("paid", PaymentServiceEnum.PAG_SEGURO.isPaid(newEvent.getStatus()));
						updatePayment.setParameter("cancelled", PaymentServiceEnum.PAG_SEGURO.isCancelled(newEvent.getStatus()));
						updatePayment.setParameter("readyForAccounting", PaymentServiceEnum.PAG_SEGURO.isReadyForAccounting(newEvent.getStatus()));
						updatePayment.executeUpdate();
					}
					else {
						result.use(Results.http()).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Não foi possível processar notificação de pagamento");
					}
				} catch (final Exception e) {
					log.error("Erro processando notificação de pagamento", e);
					result.use(Results.http()).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro processando notificação de pagamento: " + e.getClass().getName());
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
