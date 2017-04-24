//package ajuda.ai.backend.v0;
//
//import java.util.HashMap;
//
//import javax.inject.Inject;
//
//import org.slf4j.Logger;
//
//import ajuda.ai.util.StringUtils;
//import br.com.caelum.vraptor.Controller;
//import br.com.caelum.vraptor.Path;
//import br.com.caelum.vraptor.Result;
//
//@Controller
//@Path("/redirect")
//public class PostRedirectController {
//	@Inject
//	private Result result;
//	
//	@Inject
//	private Logger log;
//	
//	@Path("/post")
//	public void postRedirect(final String action, final HashMap<String, Object> params, final String charset) {
//		if (StringUtils.isEmpty(action) || params == null || params.isEmpty()) {
//			result.notFound();
//		}
//		else {
//			result.include("action", action);
//			result.include("paramMap", params);
//			result.include("charset", charset);
//		}
//	}
//}
