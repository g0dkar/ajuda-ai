//package ajuda.ai.backend.v0.admin;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.slf4j.Logger;
//
//import ajuda.ai.backend.v0.util.PersistenceService;
//import ajuda.ai.util.StringUtils;
//import ajuda.ai.util.keycloak.KeycloakUser;
//import br.com.caelum.vraptor.Result;
//import br.com.caelum.vraptor.serialization.Serializer;
//import br.com.caelum.vraptor.validator.I18nMessage;
//import br.com.caelum.vraptor.validator.Validator;
//import br.com.caelum.vraptor.view.Results;
//
///**
// * Classe abstrata com um setup básico de utilitários para o Admin. Lembre de implementar um
// * construtor {@code default} sem parâmetros por causa do CDI.
// * 
// * @author Rafael M. Lins
// *
// */
//public abstract class AdminController {
//	protected final Logger log;
//	protected final Result result;
//	protected final PersistenceService ps;
//	protected final KeycloakUser user;
//	protected final HttpServletRequest request;
//	protected final Validator validator;
//	
//	public AdminController(final Logger log, final Result result, final PersistenceService ps, final KeycloakUser user, final HttpServletRequest request, final Validator validator) {
//		this.log = log;
//		this.result = result;
//		this.ps = ps;
//		this.user = user;
//		this.request = request;
//		this.validator = validator;
//		
//		if (result != null && user != null) {
//			result.include("user", user);
//		}
//	}
//	
//	/**
//	 * Adds a {@link Validator validation} {@link I18nMessage i18n error}
//	 * 
//	 * @param key
//	 *            The i18n key
//	 * @param params
//	 *            Optional params for the i18n message
//	 */
//	protected void error(final String key, final Object... params) {
//		validator.add(new I18nMessage("error", key, params));
//	}
//	
//	/**
//	 * @return <code>true</code> if this request was created by a XMLHttpRequest object or has an
//	 *         "ajax" parameter set to <code>true</code> (in other words: "if it's AJAX or not")
//	 */
//	protected boolean isAjaxRequest() {
//		final boolean ajaxParam = StringUtils.parseBoolean(request.getParameter("ajax"), false);
//		final boolean xhrRequest = request.getHeader("X-Requested-With") != null ? request.getHeader("X-Requested-With").equalsIgnoreCase("XMLHttpRequest") : false;
//		if (log.isDebugEnabled()) {
//			log.debug("Checking if this request came from a XMLHttpRequest object or had the request parameter \"ajax\" set to true: " + (ajaxParam || xhrRequest));
//		}
//		return ajaxParam || xhrRequest;
//	}
//	
//	/**
//	 * Sends the specified object encoded as JSON as the response of the current request.
//	 * 
//	 * @param response
//	 *            The object to be encoded and sent
//	 */
//	protected Serializer jsonResponse(final Object response) {
//		return result.use(Results.json()).withoutRoot().from(response);
//	}
//	
//	/**
//	 * Sets up a {@link Validator} to send a JSON response if errors occurs.
//	 */
//	protected void jsonValidationErrorResponse() {
//		validator.onErrorUse(Results.json()).withoutRoot().from(validator.getErrors()).serialize();
//	}
//}
