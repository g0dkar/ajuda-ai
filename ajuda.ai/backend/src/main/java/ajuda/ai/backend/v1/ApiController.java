package ajuda.ai.backend.v1;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.serialization.Serializer;
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
	
	protected void response(final Object object) {
		if (object == null) {
			result.nothing();
		}
		else {
			serializer(object).recursive().serialize();
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
