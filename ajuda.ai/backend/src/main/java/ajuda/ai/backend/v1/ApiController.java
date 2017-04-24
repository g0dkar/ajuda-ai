package ajuda.ai.backend.v1;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.serialization.Serializer;
import br.com.caelum.vraptor.validator.Message;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;

/**
 * Classe com algumas coisas importantes para os outros controllers da API
 * @author Rafael Lins
 *
 */
public abstract class ApiController {
	protected Logger log;
	protected Result result;
	protected Validator validator;
	protected HttpServletRequest request;
	
	protected boolean errorResponse() {
		if (validator.hasErrors()) {
			final List<Message> errors = validator.getErrors();
			
			if ("application/xml".equals(request.getContentType())) {
				validator.onErrorUse(Results.xml()).from(errors).serialize();
			}
			else {
				validator.onErrorUse(Results.json()).withoutRoot().from(errors).serialize();
			}
		}
		
		return validator.hasErrors();
	}
	
	protected void response() {
		if (!errorResponse()) {
			result.nothing();
		}
	}
	
	protected void response(final Object object) {
		if (!errorResponse()) {
			if (object == null) {
				result.nothing();
			}
			else {
				serializer(object).recursive().serialize();
			}
		}
	}
	
	protected Serializer serializer(final Object object) {
		if ("application/xml".equals(request.getContentType())) {
			return result.use(Results.xml()).from(object);
		}
		else {
			return result.use(Results.json()).withoutRoot().from(object);
		}
	}
}
