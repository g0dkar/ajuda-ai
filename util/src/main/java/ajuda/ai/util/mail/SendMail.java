package ajuda.ai.util.mail;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;

/**
 * Classe que envia e-mails
 * 
 * @author Rafael M. Lins
 *
 */
@ApplicationScoped
public class SendMail {
	@Inject
	private Logger log;
	
	@Resource(mappedName = "java:/mail/ajuda-ai")
	private Session session;
	
	/**
	 * Sends an e-mail as a {@link Future}. Might not block everything. Use this if you need to know if the e-mail was sent successfully later on.
	 * @return {@link Future} to an invokation of {@link #send(String, String, String, String, Map)}
	 */
	public Future<Boolean> sendFuture(final String from, final String email, final String subject, final String template, final Map<String, String> templateValues) {
		return new FutureTask<Boolean>(() -> send(from, email, subject, template, templateValues));
	}
	
	/**
	 * Sends an e-mail WITHOUT blocking everything.
	 * @return {@link Thread} used to {@link #send(String, String, String, String, Map) send} the email
	 */
	public Thread sendAsync(final String from, final String email, final String subject, final String template, final Map<String, String> templateValues) {
		final Thread t = new Thread(() -> send(from, email, subject, template, templateValues));
		t.start();
		return t;
	}
	
	/**
	 * Sends an e-mail. Blocks everything while doing so.
	 */
	public boolean send(final String from, final String emails, final String subject, final String template, final Map<String, String> templateValues) {
		return send(from, parseAddresses(emails), subject, template, templateValues);
	}
	
	/**
	 * @return {@code true} if the e-mail was sent successfully
	 */
	public boolean send(final String from, final InternetAddress[] emails, final String subject, final String template, final Map<String, String> templateValues) {
		try {
			final StringBuffer html;
			
			if (templateValues != null && !templateValues.isEmpty()) {
				html = new StringBuffer();
				
				final Matcher matcher = Pattern.compile("\\$\\{([^}]+)\\}").matcher(template);
				
				while (matcher.find()) {
					matcher.appendReplacement(html, templateValues.get(matcher.group(1)));
				}
				
				matcher.appendTail(html);
			}
			else {
				html = new StringBuffer(template);
			}
			
			final Message msg = new MimeMessage(session);
			msg.setSubject(subject);
			msg.setFrom(InternetAddress.parse(from)[0]);
			msg.setRecipients(emails.length == 1 ? Message.RecipientType.TO : Message.RecipientType.BCC, emails);
			msg.setContent(html.toString(), "text/html; charset=utf-8");
			msg.setSentDate(new Date());
			
			Transport.send(msg);
			
			return true;
		} catch (final Exception me) {
			log.error("Error while sending e-mail", me);
		}
		
		return false;
	}
	
	private static final Pattern EMAIL_PATTERN = Pattern.compile("(.+<)?([^@]+@[^@>]+)(>.*)?");
	/**
	 * Try the best we can to parse an e-mail address list into a {@link List} of {@link InternetAddress}.
	 * 
	 * @param addresses The e-mail addresses list
	 * @return A {@link List} of {@link InternetAddress}.
	 */
	private InternetAddress[] parseAddresses(final String addresses) {
		try {
			return InternetAddress.parse(addresses, false);
		}
		catch (final Exception e) {
			final List<InternetAddress> addrs = new LinkedList<InternetAddress>();
			final String[] parts = addresses.split("\\s*[,;\\s]\\s*");
			
			for (final String address : parts) {
				try {
					final Matcher matcher = EMAIL_PATTERN.matcher(address);
					addrs.add(InternetAddress.parse(matcher.group(2).trim())[0]);
				} catch (final Exception e2) {
					if (log.isErrorEnabled()) {
						log.error("Error while parsing e-mail address: \"" + address + "\"", e);
					}
				}
			}
			
			return (InternetAddress[]) addrs.toArray();
		}
	}
}
