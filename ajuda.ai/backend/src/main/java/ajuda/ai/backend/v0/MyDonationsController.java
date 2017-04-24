//package ajuda.ai.backend.v0;
//
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.text.DateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Locale;
//import java.util.Map;
//import java.util.regex.Pattern;
//
//import javax.inject.Inject;
//import javax.servlet.http.HttpServletRequest;
//import javax.transaction.Transactional;
//
//import org.mindrot.jbcrypt.BCrypt;
//import org.slf4j.Logger;
//
//import ajuda.ai.backend.v0.util.Configuration;
//import ajuda.ai.backend.v0.util.PersistenceService;
//import ajuda.ai.model.billing.Payment;
//import ajuda.ai.model.institution.Helper;
//import ajuda.ai.model.institution.HelperLoginRequest;
//import ajuda.ai.util.StringUtils;
//import ajuda.ai.util.keycloak.KeycloakUser;
//import ajuda.ai.util.mail.SendMail;
//import br.com.caelum.vraptor.Consumes;
//import br.com.caelum.vraptor.Controller;
//import br.com.caelum.vraptor.Get;
//import br.com.caelum.vraptor.Path;
//import br.com.caelum.vraptor.Post;
//import br.com.caelum.vraptor.Result;
//import br.com.caelum.vraptor.validator.SimpleMessage;
//import br.com.caelum.vraptor.validator.Validator;
//
///**
// * Controller da área do doador
// * 
// * @author Rafael Lins - g0dkar
// *
// */
//@Controller
//@Path("/minhas-doacoes")
//public class MyDonationsController {
//	private static final String SESSION_PARAM = "LOGGED_HELPER_ID";
//	private static final String TOKEN_SESSION_PARAM = "LOGGED_HELPER_ID_TOKEN";
//	private static String TEMPLATE = null;
//	
//	private final Logger log;
//	private final Result result;
//	private final Helper helper;
//	private final Locale locale;
//	private final Configuration conf;
//	private final PersistenceService ps;
//	private final SendMail sendmail;
//	private final Validator validator;
//	private final HttpServletRequest request;
//	
//	/** @deprecated CDI */ @Deprecated
//	MyDonationsController() { this(null, null, null, null, null, null, null, null, null); }
//	
//	@Inject
//	public MyDonationsController(final Logger log, final Result result, final Locale locale, final HttpServletRequest request, final Configuration conf, final KeycloakUser user, final PersistenceService ps, final SendMail sendmail, final Validator validator) {
//		this.log = log;
//		this.result = result;
//		this.conf = conf;
//		this.ps = ps;
//		this.sendmail = sendmail;
//		this.validator = validator;
//		this.request = request;
//		this.locale = locale;
//		
//		if (result != null) {
//			result.include("user", user);
//			result.include("cdn", conf.get("cdn", request.getContextPath()));
//			
//			final Long helperId = (Long) request.getSession().getAttribute(SESSION_PARAM);
//			if (helperId != null) {
//				helper = ps.find(Helper.class, helperId);
//			}
//			else {
//				helper = null;
//			}
//		}
//		else {
//			helper = null;
//		}
//	}
//	
//	@Path("/")
//	public void myDonations() {
//		if (helper == null) {
//			result.redirectTo(this).myDonationsLogin();
//		}
//		else {
//			result.include("helper", helper);
//			result.include("payments", ps.createQuery("FROM Payment p JOIN FETCH p.institution WHERE helper = :helper ORDER BY timestamp DESC").setParameter("helper", helper).getResultList());
//		}
//	}
//	
//	@Get("/entrar")
//	public void myDonationsLogin() {
//		if (helper != null) {
//			result.redirectTo(this).myDonations();
//		}
//	}
//	
//	@Transactional
//	@Post("/entrar")
//	@Consumes({ "application/json", "application/x-www-form-urlencoded" })
//	public void doLogin(final String email, final String password, final String ml) {
//		log.info("email = {}, password = {}, ml = {}", email, password, ml);
//		
//		if (helper != null) {
//			result.redirectTo(this).myDonations();
//		}
//		else {
//			if (!StringUtils.isBlank(email)) {
//				final Helper helper = (Helper) ps.createQuery("FROM Helper WHERE LOWER(email) = LOWER(:email)").setParameter("email", email.trim()).getSingleResult();
//				
//				if (helper != null) {
//					if (StringUtils.parseBoolean(ml, false)) {
//						final Calendar maxTime = Calendar.getInstance();
//						maxTime.add(Calendar.HOUR_OF_DAY, -1);
//						
//						HelperLoginRequest loginRequest = (HelperLoginRequest) ps.createQuery("FROM HelperLoginRequest WHERE timestamp >= :maxTime AND active = true AND helper = :helper").setParameter("maxTime", maxTime.getTime()).setParameter("helper", helper).getSingleResult();
//						
//						if (loginRequest == null) {
//							loginRequest = new HelperLoginRequest();
//							loginRequest.setTimestamp(new Date());
//							loginRequest.setHelper(helper);
//							loginRequest.setActive(true);
//						}
//						
//						ps.createQuery("UPDATE HelperLoginRequest SET active = false WHERE helper = :helper AND active = true").setParameter("helper", helper).executeUpdate();
//						
//						if (loginRequest.getId() == null) {
//							ps.persist(loginRequest);
//						}
//						else {
//							ps.merge(loginRequest);
//						}
//						
//						if (sendLoginRequestMail(loginRequest)) {
//							result.include("messageLogin", "Para fazer continuar, siga as instruções que enviamos ao seu e-mail :)");
//							result.redirectTo(this).myDonations();
//						}
//						else {
//							validator.add(new SimpleMessage("error", "Não conseguimos enviar o e-mail para você. Por favor, aguarde alguns instantes e tente novamente."));
//						}
//					}
//					else {
//						if (!StringUtils.isEmpty(password)) {
//							if (BCrypt.checkpw(password, helper.getPassword())) {
//								request.getSession().setAttribute(SESSION_PARAM, helper.getId());
//								request.getSession().removeAttribute(TOKEN_SESSION_PARAM);
//								result.redirectTo(this).myDonations();
//							}
//							else {
//								validator.add(new SimpleMessage("error", "E-mail e senha não conferem"));
//							}
//						}
//						else {
//							validator.add(new SimpleMessage("error", "Por favor, digite sua senha"));
//						}
//					}
//				}
//				else {
//					validator.add(new SimpleMessage("error", "Este E-mail (ainda) não fez uma doação"));
//				}
//			}
//			else {
//				validator.add(new SimpleMessage("error", "E-mail e senha não conferem"));
//			}
//		}
//		
//		if (validator.hasErrors()) {
//			validator.onErrorForwardTo(this).myDonationsLogin();
//		}
//	}
//	
//	@Transactional
//	@Path("/mail-login")
//	@Consumes({ "application/json", "application/x-www-form-urlencoded" })
//	public void loginFromMail(final String t) {
//		if (helper == null) {
//			if (t != null && t.matches("[a-f0-9]{32}")) {
//				final Calendar minTimestamp = Calendar.getInstance();
//				minTimestamp.add(Calendar.HOUR_OF_DAY, -1);
//				log.info("minTimestamp = {}", minTimestamp.getTime());
//				final HelperLoginRequest loginRequest = (HelperLoginRequest) ps.createQuery("FROM HelperLoginRequest WHERE id = :t AND active = true AND timestamp BETWEEN :minTimestamp AND NOW() AND useTimestamp IS NULL").setParameter("t", t).setParameter("minTimestamp", minTimestamp.getTime()).getSingleResult();
//				if (loginRequest != null) {
//					ps.createQuery("UPDATE HelperLoginRequest SET active = false, useTimestamp = NOW() WHERE id = :id").setParameter("id", loginRequest.getId()).executeUpdate();
//					request.getSession().setAttribute(SESSION_PARAM, loginRequest.getHelper().getId());
//					request.getSession().setAttribute(TOKEN_SESSION_PARAM, t);
//				}
//				else {
//					validator.add(new SimpleMessage("error", "Você demorou demais para utilizar o link que mandamos ao seu e-mail. Por favor, comece o processo novamente. Você tem 1 hora para fazer login após o pedido de login via e-mail."));
//				}
//			}
//			else {
//				validator.add(new SimpleMessage("error", "Token inválido."));
//			}
//		}
//		
//		if (validator.hasErrors()) {
//			validator.onErrorRedirectTo(this).myDonations();
//		}
//		else {
//			result.redirectTo(this).myDonations();
//		}
//	}
//	
//	@Get("/dados")
//	public void myData() {
//		if (helper != null) {
//			result.include("helper", helper);
//		}
//		else {
//			result.redirectTo(this).myDonationsLogin();
//		}
//	}
//	
//	@Transactional
//	@Post("/dados")
//	@Consumes({ "application/json", "application/x-www-form-urlencoded" })
//	public void updateData(final String name, final String email, final String nationalid, final String anonymous) {
//		if (helper != null) {
//			final boolean emailExists = ((Number) ps.createQuery("SELECT count(*) FROM Helper WHERE id <> :id AND LOWER(email) = LOWER(:email)").setParameter("id", helper.getId()).setParameter("email", email.trim()).getSingleResult()).intValue() > 0;
//			
//			if (!emailExists) {
//				helper.setName(name);
//				helper.setEmail(email.toLowerCase().trim());
//				helper.setNationalId(nationalid);
//				helper.setAnonymous(StringUtils.parseBoolean(anonymous, helper.isAnonymous()));
//				
//				if (!validator.validate(helper).hasErrors()) {
//					ps.merge(helper);
//					result.include("messageLogin", "Alterações salvas com sucesso!");
//					result.redirectTo(this).myData();
//				}
//			}
//			else {
//				validator.add(new SimpleMessage("error", "O E-mail que você está tentando usar já está cadastrado em nosso sistema"));
//			}
//		}
//		else {
//			result.redirectTo(this).myDonationsLogin();
//		}
//		
//		if (validator.hasErrors()) {
//			validator.onErrorForwardTo(this).myData();
//		}
//	}
//	
//	@Transactional
//	@Post("/mudar-senha")
//	@Consumes({ "application/json", "application/x-www-form-urlencoded" })
//	public void changePassword(final String newpass) {
//		result.redirectTo(this).myDonations();
//		
//		if (helper != null) {
//			if (!StringUtils.isBlank(newpass)) {
//				if (newpass.length() < 6) { validator.add(new SimpleMessage("error", "Sua Nova Senha deve ter pelo menos 6 caracteres de tamanho!")); }
//				
//				// Vamos dar uma nota à senha:
//				int newPasswordScore = 0;
//				
//				// 2 Minúsculas +1
//				if (Pattern.compile("[a-z]").matcher(newpass).find()) {
//					newPasswordScore++;
//				}
//				// 2 Maiúsculas +1
//				if (Pattern.compile("[A-Z]").matcher(newpass).find()) {
//					newPasswordScore++;
//				}
//				// 1 ou mais números +1
//				if (Pattern.compile("[0-9]").matcher(newpass).find()) {
//					newPasswordScore++;
//				}
//				// Caracteres especiais +1
//				if (Pattern.compile("\\p{Punct}").matcher(newpass).find()) {
//					newPasswordScore++;
//				}
//				
//				if (newPasswordScore < 3) {
//					validator.add(new SimpleMessage("error", "Além de 6 ou mais caracteres, por sua segurança as senhas devem ter <strong>pelo menos 3</strong> desses: Maiúsculas, minúsculas, números e caracteres especiais."));
//				}
//				
//				if (!validator.hasErrors()) {
//					ps.createQuery("UPDATE Helper SET password = :password WHERE id = :id").setParameter("password", BCrypt.hashpw(newpass, BCrypt.gensalt(conf.get("bcrypt.rounds", 10)))).setParameter("id", helper.getId()).executeUpdate();
//					result.include("messageLogin", "Senha alterada com sucesso!");
//				}
//			}
//			else {
//				validator.add(new SimpleMessage("error", "Por favor, preencha sua Nova Senha!"));
//			}
//		}
//		else {
//			result.redirectTo(this).myDonations();
//		}
//		
//		if (validator.hasErrors()) {
//			validator.onErrorForwardTo(this).myDonations();
//		}
//	}
//	
//	private boolean sendLoginRequestMail(final HelperLoginRequest loginRequest) {
//		final String template = loadTemplate();
//		
//		if (template != null) {
//			final String subject = conf.get("mailLogin.subject", "Login no Ajuda.Ai");
//			final Map<String, String> templateValues = new HashMap<>(8);
//			templateValues.put("subject", subject);
//			templateValues.put("title", conf.get("mailLogin.title", "Login no Ajuda.Ai"));
//			templateValues.put("content", StringUtils.markdown(conf.get("mailLogin.content", "Recebemos um pedido de login no Ajuda.Ai com seu e-mail.\n\nPara fazer login, basta **clicar no botão abaixo**.\n\n**Você tem até " + DateFormat.getDateInstance(DateFormat.FULL, locale).format(new Date()) + " para fazer login**\n\n_Se não foi você que fez esse pedido, desconsidere. Está tudo bem, ninguém tem acesso a sua conta se não clicar nesse botão aqui_ ;)")));
//			templateValues.put("actionText", conf.get("mailLogin.actionText", "Continuar no Ajuda.Ai"));
//			templateValues.put("actionUrl", "https://ajuda.ai/minhas-doacoes/mail-login?t=" + loginRequest.getId());
//			templateValues.put("timestamp", DateFormat.getTimeInstance(DateFormat.FULL, locale).format(new Date()));
//			templateValues.put("email", loginRequest.getHelper().getEmail());
//			templateValues.put("sendReason", "Pedido de Login sem Senha no Ajuda.Ai");
//			
//			sendmail.sendAsync(conf.get("mail.from", "no-reply@ajuda.ai"), loginRequest.getHelper().getEmail(), subject, template, templateValues);
//			return true;
//		}
//		else {
//			return false;
//		}
//	}
//	
//	private String loadTemplate() {
//		if (TEMPLATE == null) {
//			try {
//				final InputStream templateIn = getClass().getClassLoader().getResourceAsStream("mail-template.min.html");
//				final StringBuilder str = new StringBuilder();
//				final BufferedReader br = new BufferedReader(new InputStreamReader(templateIn));
//				String line;
//				while ((line = br.readLine()) != null) {
//					str.append(line.replaceAll("\\t", ""));
//					str.append("\n");
//				}
//				
//				TEMPLATE = str.toString();
//			} catch (final Exception e) {
//				log.error("Erro lendo template", e);
//			}
//		}
//		
//		return TEMPLATE;
//	}
//	
//	@Path("/recibo/{id:[a-f0-9]+}")
//	public void reciept(final String id) {
//		if (helper == null) {
//			result.redirectTo(this).myDonationsLogin();
//		}
//		else {
//			final Payment payment = (Payment) ps.createQuery("FROM Payment p JOIN FETCH p.institution WHERE helper = :helper AND id = :id").setParameter("helper", helper).setParameter("id", id).getSingleResult();
//			
//			if (payment != null) {
//				result.include("helper", helper);
//				result.include("payment", payment);
//			}
//			else {
//				result.notFound();
//			}
//		}
//	}
//	
//	@Path("/enviar-recibo/{id:[a-f0-9]+}")
//	public void mailReciept(final String id) {
//		if (helper == null) {
//			result.redirectTo(this).myDonationsLogin();
//		}
//		else {
//			final Payment payment = (Payment) ps.createQuery("FROM Payment p JOIN FETCH p.institution WHERE p.helper = :helper AND p.id = :id").setParameter("helper", helper).setParameter("id", id).getSingleResult();
//			
//			if (payment != null) {
//				result.include("helper", helper);
//				result.include("payment", payment);
//			}
//			else {
//				result.notFound();
//			}
//		}
//	}
//}
