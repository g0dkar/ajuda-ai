package ajuda.ai.backend.v1.cors;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ajuda.ai.persistence.model.ConfigurationPersistence;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Options;
import br.com.caelum.vraptor.Result;

/**
 * Implementa de forma bem simples e direta o protocolo CORS (Cross-Origin Resource Sharing)
 * 
 * @author Rafael Lins
 * @see https://developer.mozilla.org/en-US/docs/Web/HTTP/Access_control_CORS
 *
 */
@Controller
public class CORSController {
	private final Result result;
	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private final ConfigurationPersistence cp;
	
	/** @deprecated CDI */ @Deprecated
	CORSController() { this(null, null, null, null); }
	
	@Inject
	public CORSController(final Result result, final HttpServletRequest request, final HttpServletResponse response, final ConfigurationPersistence cp) {
		this.result = result;
		this.request = request;
		this.response = response;
		this.cp = cp;
	}

	/**
	 * <p><code>OPTIONS /*</code>
	 * <p>Qualquer requisição feita de outro domínio vai, primeiro, fazer um {@code OPTIONS} na URL
	 * para saber quais as permissões de CORS. Esse método é o <em>catch-all</em> para qualquer chamada {@code OPTIONS}
	 * em qualquer URL. Ele responde com os cabeçalhos necessários e nada mais.
	 *
	 * @see Options
	 */
	@Options("/*")
	public void cors() {
		final Instant maxAge = Instant.now().plus(cp.get("cors.maxAgeDays", 7L), ChronoUnit.DAYS);
		response.setDateHeader("Access-Control-Max-Age", maxAge.toEpochMilli());
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, Origin, Authorization, Accept, Client-Security-Token, Accept-Encoding");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
		result.nothing();
	}
}
