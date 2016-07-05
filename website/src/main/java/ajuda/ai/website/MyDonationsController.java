package ajuda.ai.website;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;

import ajuda.ai.model.institution.Helper;
import ajuda.ai.model.institution.HelperLoginRequest;
import ajuda.ai.util.StringUtils;
import ajuda.ai.util.keycloak.KeycloakUser;
import ajuda.ai.util.mail.SendMail;
import ajuda.ai.website.util.Configuration;
import ajuda.ai.website.util.PersistenceService;
import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.SimpleMessage;
import br.com.caelum.vraptor.validator.Validator;

/**
 * Controller da área do doador
 * 
 * @author Rafael Lins - g0dkar
 *
 */
@Controller
@Path("/minhas-doacoes")
public class MyDonationsController {
	private static final String SESSION_PARAM = "LOGGED_HELPER_ID";
	private static String TEMPLATE = null;
	
	private final Logger log;
	private final Result result;
	private final Helper helper;
	private final Locale locale;
	private final Configuration conf;
	private final PersistenceService ps;
	private final SendMail sendmail;
	private final Validator validator;
	private final HttpServletRequest request;
	
	/** @deprecated CDI */ @Deprecated
	MyDonationsController() { this(null, null, null, null, null, null, null, null, null); }
	
	@Inject
	public MyDonationsController(final Logger log, final Result result, final Locale locale, final HttpServletRequest request, final Configuration conf, final KeycloakUser user, final PersistenceService ps, final SendMail sendmail, final Validator validator) {
		this.log = log;
		this.result = result;
		this.conf = conf;
		this.ps = ps;
		this.sendmail = sendmail;
		this.validator = validator;
		this.request = request;
		this.locale = locale;
		
		if (result != null) {
			result.include("user", user);
			result.include("cdn", conf.get("cdn", request.getContextPath()));
			
			final Long helperId = (Long) request.getSession().getAttribute(SESSION_PARAM);
			if (helperId != null) {
				helper = ps.find(Helper.class, helperId);
			}
			else {
				helper = null;
			}
		}
		else {
			helper = null;
		}
	}
	
	@Path("/")
	public void index() {
		if (helper == null) {
			result.redirectTo(this).login();
		}
		else {
			result.include("helper", helper);
			result.include("payments", ps.createQuery("FROM Payment p JOIN FETCH p.institution WHERE helper = :helper").getResultList());
		}
	}
	
	@Get("/entrar")
	public void login() {
		if (helper != null) {
			result.redirectTo(this).index();
		}
	}
	
	@Transactional
	@Post("/entrar")
	@Consumes({ "application/json", "application/x-www-form-urlencoded" })
	public void doLogin(final String email, final String password, final String ml) {
		validator.onErrorForwardTo(this).login();
		
		if (helper != null) {
			result.redirectTo(this).index();
		}
		else {
			if (!StringUtils.isBlank(email)) {
				final Helper helper = (Helper) ps.createQuery("FROM Helper WHERE LOWER(email) = LOWER(:email)").setParameter("email", email.trim()).getSingleResult();
				
				if (helper != null) {
					if (StringUtils.parseBoolean(ml, false)) {
						final Calendar maxTime = Calendar.getInstance();
						maxTime.add(Calendar.HOUR_OF_DAY, -1);
						
						HelperLoginRequest loginRequest = (HelperLoginRequest) ps.createQuery("FROM HelperLoginRequest WHERE timestamp >= :maxTime AND active = true AND helper = :helper").setParameter("maxTime", maxTime.getTime()).setParameter("helper", helper).getSingleResult();
						
						if (loginRequest == null) {
							loginRequest = new HelperLoginRequest();
							loginRequest.setTimestamp(new Date());
							loginRequest.setHelper(helper);
							loginRequest.setActive(true);
						}
						
						ps.createQuery("UPDATE HelperLoginRequest SET active = false WHERE helper = :helper AND active = true").setParameter("helper", helper).executeUpdate();
						
						if (loginRequest.getId() == null) {
							ps.persist(loginRequest);
						}
						else {
							ps.merge(loginRequest);
						}
						
						if (sendLoginRequestMail(loginRequest)) {
							result.include("messageLogin", "Para fazer continuar, siga as instruções que enviamos ao seu e-mail :)");
							result.redirectTo(this).index();
						}
						else {
							validator.add(new SimpleMessage("error", "Não conseguimos enviar o e-mail para você. Por favor, aguarde alguns instantes e tente novamente."));
						}
					}
					else if (!StringUtils.isEmpty(password)) {
						if (BCrypt.checkpw(password, helper.getPassword())) {
							request.getSession().setAttribute(SESSION_PARAM, helper.getId());
							result.redirectTo(this).index();
						}
						else {
							validator.add(new SimpleMessage("error", "E-mail e senha não conferem"));
						}
					}
				}
				else {
					validator.add(new SimpleMessage("error", "E-mail e senha não conferem"));
				}
			}
			else {
				validator.add(new SimpleMessage("error", "E-mail e senha não conferem"));
			}
		}
	}
	
	@Transactional
	@Post("/mail-login")
	@Consumes({ "application/json", "application/x-www-form-urlencoded" })
	public void loginFromMail(final String t) {
		validator.onErrorForwardTo(this).index();
		
		if (helper != null) {
			if (t != null && t.matches("[a-f0-9]{32}")) {
				final HelperLoginRequest loginRequest = (HelperLoginRequest) ps.createQuery("FROM HelperLoginRequest WHERE id = :t AND active = false AND useTimestamp IS NOT NULL").setParameter("t", t).getSingleResult();
			}
		}
	}
	
	@Transactional
	@Post("/mudar-senha")
	@Consumes({ "application/json", "application/x-www-form-urlencoded" })
	public void setPasswordFromMail(final String currentPassword, final String newPassword, final String t) {
		validator.onErrorForwardTo(this).index();
		result.redirectTo(this).index();
		
		if (helper != null) {
			if (t != null && t.matches("[a-f0-9]{32}")) {
				final HelperLoginRequest loginRequest = (HelperLoginRequest) ps.createQuery("FROM HelperLoginRequest WHERE id = :t AND active = false AND useTimestamp IS NOT NULL").setParameter("t", t).getSingleResult();
				
				if (loginRequest != null) {
					final Calendar timeLimit = Calendar.getInstance();
					timeLimit.add(Calendar.MINUTE, -30);
					
					if (loginRequest.getUseTimestamp().after(timeLimit.getTime())) {
						ps.createQuery("UPDATE Helper SET password = :password WHERE id = :id").setParameter("password", BCrypt.hashpw(newPassword, BCrypt.gensalt(conf.get("bcrypt.rounds", 10)))).setParameter("id", helper.getId()).executeUpdate();
					}
					else {
						validator.add(new SimpleMessage("error", "Desculpe, mas por segurança a mudança de senha só é permitida por 30 minutos após o login."));
					}
				}
				else {
					validator.add(new SimpleMessage("error", "Token Inválido."));
				}
			}
			else {
				if (BCrypt.checkpw(currentPassword, helper.getPassword())) {
					ps.createQuery("UPDATE Helper SET password = :password WHERE id = :id").setParameter("password", BCrypt.hashpw(newPassword, BCrypt.gensalt(conf.get("bcrypt.rounds", 10)))).setParameter("id", helper.getId()).executeUpdate();
					result.include("messageLogin", "Senha alterada com sucesso!");
				}
				else {
					validator.add(new SimpleMessage("error", "Senha errada. Por favor, tente novamente."));
				}
			}
		}
		else {
			result.redirectTo(this).index();
		}
	}
	
	private boolean sendLoginRequestMail(final HelperLoginRequest loginRequest) {
		final String template = loadTemplate();
		
		if (template != null) {
			final String subject = conf.get("mailLogin.subject", "Login no Ajuda.Ai");
			final Map<String, String> templateValues = new HashMap<>();
			templateValues.put("subject", subject);
			templateValues.put("title", conf.get("mailLogin.title", "Login no Ajuda.Ai"));
			templateValues.put("content", StringUtils.markdown(conf.get("mailLogin.content", "Recebemos um pedido de login no Ajuda.Ai com seu e-mail.\n\nPara fazer login, basta clicar no **botão abaixo**. Se não foi você que fez esse pedido, desconsidere. Está tudo bem, ninguém tem acesso a sua conta se não clicar nesse botão aqui :)")));
			templateValues.put("actionText", conf.get("mailLogin.actionText", "Continuar no Ajuda.Ai"));
			templateValues.put("actionUrl", "https://ajuda.ai/minhas-doacoes/mail-login?t=" + loginRequest.getId());
			templateValues.put("timestamp", DateFormat.getTimeInstance(DateFormat.FULL, locale).format(new Date()));
			templateValues.put("email", loginRequest.getHelper().getEmail());
			templateValues.put("sendReason", "Pedido de Login sem Senha no Ajuda.Ai");
			
			sendmail.sendAsync(conf.get("mail.from"), loginRequest.getHelper().getEmail(), subject, template, templateValues);
			return true;
		}
		else {
			return false;
		}
	}
	
	private String loadTemplate() {
		if (TEMPLATE == null) {
			try {
				final InputStream templateIn = getClass().getClassLoader().getResourceAsStream("mail-template.min.html");
				final StringBuilder str = new StringBuilder();
				final BufferedReader br = new BufferedReader(new InputStreamReader(templateIn));
				String line;
				while ((line = br.readLine()) != null) {
					str.append(line.replaceAll("\\t", ""));
					str.append("\n");
				}
				
				TEMPLATE = str.toString();
			} catch (final Exception e) {
				log.error("Erro lendo template", e);
			}
		}
		
		return TEMPLATE;
	}
}
