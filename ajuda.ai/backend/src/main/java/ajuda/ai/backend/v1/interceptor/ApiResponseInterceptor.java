package ajuda.ai.backend.v1.interceptor;

import javax.inject.Inject;

import org.slf4j.Logger;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;

@Intercepts
public class ApiResponseInterceptor implements Interceptor {
	private final Logger log;
	private final Result result;
	
	/** @deprecated CDI */ @Deprecated
	ApiResponseInterceptor() { this(null, null); }
	
	@Inject
	public ApiResponseInterceptor(final Logger log, final Result result) {
		this.log = log;
		this.result = result;
	}
	
	@Override
	public void intercept(final InterceptorStack stack, final ControllerMethod method, final Object controllerInstance) throws InterceptionException {
		stack.next(method, controllerInstance);
		log.info("Após o método. Result = {} --- Result Params = {}", result, result.included());
	}

	@Override
	public boolean accepts(final ControllerMethod method) {
		return true;
	}
}
