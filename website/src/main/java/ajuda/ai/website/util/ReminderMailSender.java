package ajuda.ai.website.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import ajuda.ai.model.institution.InstitutionHelper;
import ajuda.ai.util.mail.SendMail;

/**
 * Envia lembretes de doação
 * 
 * @author Rafael Lins
 *
 */
@ApplicationScoped
public class ReminderMailSender {
	@Inject
	private SendMail sendmail;
	private String template;
	
	@PostConstruct
	public void loadTemplate() throws IOException {
		final InputStream templateIn = getClass().getClassLoader().getResourceAsStream("reminder-email.html");
		final StringBuilder str = new StringBuilder();
		final BufferedReader br = new BufferedReader(new InputStreamReader(templateIn));
		String line;
		while ((line = br.readLine()) != null) {
			str.append(line.replaceAll("\\t", ""));
		}
		
		template = str.toString();
	}
	
	/**
	 * Envia um e-mail de lembrete. Bloqueia o código.
	 * 
	 * @param helper Para quem o lembrete será enviado
	 * @param async TODO
	 * 
	 * @return {@code true} caso o lembrete tenha sido enviado. {@code false} caso essa pessoa não deva receber lembretes (sem token ou token velho demais)
	 */
	public boolean sendReminder(final InstitutionHelper helper, final boolean async) {
		if (helper.getReminderToken() != null) {
			final Calendar now = Calendar.getInstance();
			now.set(Calendar.HOUR_OF_DAY, 0);
			now.set(Calendar.MINUTE, 0);
			now.set(Calendar.SECOND, 0);
			now.set(Calendar.MILLISECOND, 0);
			
			final Calendar timeLimit = Calendar.getInstance();
			timeLimit.setTime(helper.getReminderTokenDate());
			timeLimit.add(Calendar.MONTH, 1);
			timeLimit.set(Calendar.HOUR_OF_DAY, 0);
			timeLimit.set(Calendar.MINUTE, 0);
			timeLimit.set(Calendar.SECOND, 0);
			timeLimit.set(Calendar.MILLISECOND, 0);
			
			if (now.compareTo(timeLimit) <= 0) {
				final String subject = "Lembrete de Doação";
				
				final Map<String, String> templateValues = new HashMap<>(5);
				templateValues.put("name", helper.getName());
				templateValues.put("institution", helper.getInstitution().getName());
				templateValues.put("subject", subject);
				templateValues.put("token", helper.getReminderToken());
				templateValues.put("donateLink", "https://ajuda.ai/" + helper.getInstitution().getSlug() + "/api/donate/" + helper.getId());
				
				if (async) {
					sendmail.sendAsync("lembrete-de-doacao@ajuda.ai", helper.getEmail(), subject, template, templateValues);
				}
				else {
					sendmail.send("lembrete-de-doacao@ajuda.ai", helper.getEmail(), subject, template, templateValues);
				}
				
				return true;
			}
		}
		
		return false;
	}
}
