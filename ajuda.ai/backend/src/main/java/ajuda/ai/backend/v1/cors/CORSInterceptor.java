package ajuda.ai.backend.v1.cors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.BeforeCall;
import br.com.caelum.vraptor.Intercepts;

/**
 * Intercepta requisições e adiciona cabeçalhos CORS "burros".
 * 
 * @author Rafael Lins
 *
 */
@Intercepts
@ApplicationScoped
public class CORSInterceptor {
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	
	/** @deprecated CDI */ @Deprecated
	CORSInterceptor() { this(null, null); }
	
	@Inject
	public CORSInterceptor(final HttpServletRequest request, final HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}
	
	@BeforeCall
	public void addCorsHeaders() {
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Allow-Credentials", "true");
	}
}
