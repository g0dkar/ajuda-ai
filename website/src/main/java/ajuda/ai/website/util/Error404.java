package ajuda.ai.website.util;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Specializes;
import javax.inject.Inject;
import javax.servlet.FilterChain;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.DefaultControllerNotFoundHandler;
import br.com.caelum.vraptor.controller.HttpMethod;
import br.com.caelum.vraptor.events.ControllerNotFound;
import br.com.caelum.vraptor.http.MutableRequest;
import br.com.caelum.vraptor.http.MutableResponse;
import br.com.caelum.vraptor.http.route.ControllerNotFoundException;
import br.com.caelum.vraptor.http.route.Router;
import br.com.caelum.vraptor.view.Results;

/**
 * Tenta acessar uma URL colocando um "/" no final antes de dar um 404.
 * 
 * @author Rafael Lins
 *
 */
@Specializes
@RequestScoped
public class Error404 extends DefaultControllerNotFoundHandler {
	private final Router router;
	private final Result result;
	
	@Inject
	public Error404(final Router router, final Result result, final Event<ControllerNotFound> event) {
		super(event);
		this.router = router;
		this.result = result;
	}
	
	/**
	 * @deprecated CDI eyes only
	 */
	@Deprecated
	public Error404() {
		this(null, null, null);
	}
	
	@Override
	public void couldntFind(final FilterChain chain, final MutableRequest request, final MutableResponse response) {
		try {
			final String uri = request.getRequestedUri();
			
			if (uri.endsWith("/")) {
				tryMovePermanentlyTo(request, uri.substring(0, uri.length() - 1));
			} else {
				tryMovePermanentlyTo(request, uri + "/");
			}
		} catch (final ControllerNotFoundException ex) {
			super.couldntFind(chain, request, response);
		}
	}
	
	private void tryMovePermanentlyTo(final MutableRequest request, final String newUri) {
		router.parse(newUri, HttpMethod.of(request), request);
		result.use(Results.status()).movedPermanentlyTo(newUri);
	}
}