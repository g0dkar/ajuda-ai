package ajuda.ai.backend.v1;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import ajuda.ai.backend.v1.recaptcha.ReCaptchaService;
import ajuda.ai.model.institution.Institution;
import ajuda.ai.payment.PaymentService;
import ajuda.ai.persistence.model.institution.InstitutionPersistence;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Validator;

@Controller
@Path("/institution")
public class InstitutionController {
	private final Logger log;
	private final Result result;
	private final Validator validator;
	private final InstitutionPersistence ps;
	private final HttpServletRequest request;
	private final PaymentService paymentService;
	private final ReCaptchaService recaptcha;
	
	@Inject
	public InstitutionController(Logger log, Result result, Validator validator, InstitutionPersistence ps, HttpServletRequest request, PaymentService paymentService, ReCaptchaService recaptcha) {
		this.log = log;
		this.result = result;
		this.validator = validator;
		this.ps = ps;
		this.request = request;
		this.paymentService = paymentService;
		this.recaptcha = recaptcha;
	}
	
	@Get("/{slug:[a-z][a-z0-9\\-]*[a-z0-9](/[a-z][a-z0-9\\-]*[a-z0-9])?}")
	public Institution get(String slug) {
		return ps.getSlug(slug);
	}
	
	@Get("/{id:\\d+}")
	public Institution get(Long id) {
		return ps.get(id);
	}
}
