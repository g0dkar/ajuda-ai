//package ajuda.ai.website.util;
//
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.annotation.PostConstruct;
//import javax.enterprise.context.ApplicationScoped;
//import javax.inject.Inject;
//
//import org.slf4j.Logger;
//
//import ajuda.ai.model.institution.Helper;
//import ajuda.ai.util.mail.SendMail;
//
///**
// * Envia lembretes de doação
// *
// * @author Rafael Lins
// *
// */
//@ApplicationScoped
//public class ReminderMailSender {
//	@Inject
//	private Logger log;
//	@Inject
//	private SendMail sendmail;
//
//	private String template;
//
//	@PostConstruct
//	public void loadTemplate() {
//		try {
//			final InputStream templateIn = getClass().getClassLoader().getResourceAsStream("reminder-email.html");
//			final StringBuilder str = new StringBuilder();
//			final BufferedReader br = new BufferedReader(new InputStreamReader(templateIn));
//			String line;
//			while ((line = br.readLine()) != null) {
//				str.append(line.replaceAll("\\t", ""));
//				str.append("\n");
//			}
//
//			template = str.toString();
//		} catch (final Exception e) {
//			log.error("Erro lendo template", e);
//		}
//	}
//
//	/**
//	 * Envia um e-mail de lembrete. Bloqueia o código.
//	 *
//	 * @param helper Para quem o lembrete será enviado
//	 * @param async TODO
//	 *
//	 * @return {@code true} caso o lembrete tenha sido enviado. {@code false} caso essa pessoa não deva receber lembretes (sem token ou token velho demais)
//	 */
//	public boolean sendReminder(final Helper helper, final boolean async) {
//		if (log.isDebugEnabled()) { log.debug("Enviando e-mail de lembrança de doação: {} (async? {})", helper, async); }
//
//		if (helper.getReminderToken() != null) {
//			final Calendar now = Calendar.getInstance();
//			now.set(Calendar.HOUR_OF_DAY, 0);
//			now.set(Calendar.MINUTE, 0);
//			now.set(Calendar.SECOND, 0);
//			now.set(Calendar.MILLISECOND, 0);
//
//			final Calendar timeLimit = Calendar.getInstance();
//			timeLimit.setTime(helper.getReminderTokenDate());
//			timeLimit.add(Calendar.MONTH, 1);
//			timeLimit.set(Calendar.HOUR_OF_DAY, 0);
//			timeLimit.set(Calendar.MINUTE, 0);
//			timeLimit.set(Calendar.SECOND, 0);
//			timeLimit.set(Calendar.MILLISECOND, 0);
//
//			if (log.isDebugEnabled()) { log.debug("Ainda está no prazo de envio? now = {}, timeLimit = {}", now.getTime(), timeLimit.getTime()); }
//
//			if (now.compareTo(timeLimit) <= 0) {
//				final String subject = "Lembrete de Doação";
//
//				final Map<String, String> templateValues = new HashMap<>(6);
//				templateValues.put("name", helper.getName());
//				templateValues.put("institution", helper.getInstitution().getName());
//				templateValues.put("subject", subject);
//				templateValues.put("token", helper.getReminderToken());
//				templateValues.put("donateLink", "https://ajuda.ai/" + helper.getInstitution().getSlug() + "/api/donate/" + helper.getId());
//				templateValues.put("unsubscribeLink", "https://ajuda.ai/" + helper.getInstitution().getSlug() + "/api/unsubscribe/" + helper.getReminderToken());
//
//				if (log.isDebugEnabled()) { log.debug("Enviando..."); }
//
//				if (async) {
//					sendmail.sendAsync("lembrete-de-doacao@ajuda.ai", helper.getEmail(), subject, template, templateValues);
//				}
//				else {
//					sendmail.send("lembrete-de-doacao@ajuda.ai", helper.getEmail(), subject, template, templateValues);
//				}
//
//				return true;
//			}
//		}
//		else if (log.isDebugEnabled()) { log.debug("Helper não tem token de lembrete. Ignorado.", helper, async); }
//
//		return false;
//	}
//}
