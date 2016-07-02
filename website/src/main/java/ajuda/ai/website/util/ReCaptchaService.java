package ajuda.ai.website.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import ajuda.ai.util.JsonUtils;
import ajuda.ai.util.StringUtils;

/**
 * Serviço para verificação do ReCaptcha
 * @author Rafael Lins
 *
 */
@RequestScoped
public class ReCaptchaService {
	private final Logger log;
	private final Configuration conf;
	private final HttpServletRequest request;
	
	/** @deprecated CDI */ @Deprecated
	ReCaptchaService() { this(null, null, null); }
	
	@Inject
	public ReCaptchaService(final Logger log, final Configuration conf, final HttpServletRequest request) {
		this.log = log;
		this.conf = conf;
		this.request = request;
	}
	
	/**
	 * Verifica um desafio captcha junto ao servidor do ReCaptcha
	 * @return Resposta do servidor (convertida de JSON para Map)
	 */
	public Map<String, Object> getRecaptchaResponse() {
		try {
			final String captchaResponse = request.getParameter("g-recaptcha-response");
			
			if (captchaResponse != null) {
				final StringBuilder payload = new StringBuilder("secret=");
				payload.append(StringUtils.encodeURLComponent(conf.get("recaptcha.secret", "secret")));
				payload.append("&response=");
				payload.append(StringUtils.encodeURLComponent(captchaResponse));
				
				if (log.isDebugEnabled()) { log.debug("Verificando ReCaptcha. Payload: {}", payload); }
				
				final URL url = new URL(conf.get("recaptcha.endpoint", "https://www.google.com/recaptcha/api/siteverify"));
				final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.getOutputStream().write(payload.toString().getBytes());
				
				String line;
				final StringBuilder response = new StringBuilder();
				final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line = br.readLine()) != null) {
					response.append(line);
				}
				
				if (log.isDebugEnabled()) { log.debug("Resposta recebida do ReCaptcha (sem quebras de linha): {}", response); }
				
				return JsonUtils.fromJson(response.toString(), Map.class);
			}
		} catch (final Exception e) {
			log.error("Erro ao verificar ReCaptcha", e);
		}
		
		return null;
	}
	
	/**
	 * Executa {@link #getRecaptchaResponse() o processo de verificação do desafio captcha} e verifica se a resposta corresponde a um sucesso.
	 * 
	 * @return {@code true} se o desafio foi passado com sucesso, {@code false} em qualquer outro caso (erro, captcha em branco, etc.)
	 */
	public boolean isRecaptchaSuccess() {
		final Map<String, Object> captchaResponse = getRecaptchaResponse();
		
		if (captchaResponse != null && !captchaResponse.isEmpty() && captchaResponse.containsKey("success") && captchaResponse.containsKey("hostname")) {
			final Boolean success = (Boolean) captchaResponse.get("success");
			final String hostname = (String) captchaResponse.get("hostname");
			
			if (log.isDebugEnabled()) { log.debug("Sucesso no Captcha? {}, Hostname: {}", success, hostname); }
			
			return success && (hostname.equals("localhost") || hostname.endsWith(conf.get("hostname", "ajuda.ai")));
		}
		
		return false;
	}
}
