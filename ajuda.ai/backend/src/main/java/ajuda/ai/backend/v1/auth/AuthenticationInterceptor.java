package ajuda.ai.backend.v1.auth;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.AroundCall;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.AcceptsWithAnnotations;
import br.com.caelum.vraptor.interceptor.SimpleInterceptorStack;
import br.com.caelum.vraptor.view.Results;

@AcceptsWithAnnotations(Auth.class)
public class AuthenticationInterceptor {
	@Inject
	private AuthenticatedUser authUser;
	
	@Inject
	private Result result;
	
	@AroundCall
    public void around(SimpleInterceptorStack stack) {
		if (authUser.isLoggedIn()) {
			stack.next();
		}
		else {
			result.use(Results.http()).sendError(HttpServletResponse.SC_UNAUTHORIZED);
		}
    }
}
