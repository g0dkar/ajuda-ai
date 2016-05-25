package ajuda.ai.util.mail;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
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

import org.markdownj.MarkdownProcessor;
import org.slf4j.Logger;

/**
 * Utility class for sending e-mails for Responde.Ai
 * 
 * @author Rafael M. Lins
 *
 */
@ApplicationScoped
public class SendMail {
	private static final MarkdownProcessor MARKDOWN = new MarkdownProcessor();
	
	private static final String[] PERGUNTAS = {
		"Por que o computador foi preso?",
		"Quem inventou a fila?",
		"O que a areia da praia falou para o mar?",
		"Por que algumas pessoas colocam o despertador do debaixo do travesseiro?",
		"O que é que se pões na mesa, parte, reparte mas não se come?",
		"O que é que se tem debaixo de um tapete do hospício?",
		"Qual é o queijo que mais sofre?",
		"O que é que anda com os pé na cabeça?",
		"O que o cavalo foi fazer no orelhão?",
		"O que entra na água e não se molha?",
		"Que é seu, mas as pessoas usam mais do que você?",
		"Quem é o rei da horta?",
		"Qual é o animal que não vale mais nada?",
		"Onde é que o boi consegue passar, mas o mosquito fica preso?",
		"O que o cirurgião e o matemático têm em comum?",
		"Qual é a palavra que só tem três letras e acaba com tudo?",
		"Qual o cúmulo da burrice?",
	};
	
	private static final String[] RESPOSTAS = {
		"Porque ele executou um programa.",
		"As formigas.",
		"\"Deixa de onda...\"",
		"Para acordar em cima da hora.",
		"Baralho.",
		"Um doido varrido.",
		"O Queijo Ralado.",
		"O Piolho.",
		"Passar um trote.",
		"A sombra.",
		"Seu nome.",
		"O Rei Polho.",
		"O javali.",
		"Na teia de aranha.",
		"Ambos vivem fazendo operações.",
		"Fim.",
		"Tirar \"par ou ímpar\" com o espelho e escolher ímpar.",
	};
	
	@Inject
	private Logger log;
	
	@Resource(mappedName = "java:/mail/RespondeAi")
	private Session session;
	
	/**
	 * Returns a {@link Future} that sends an e-mail (sort of non-blocking send)
	 * @param from TODO
	 * @param email E-mail address to send the e-mail to
	 * @param subject E-mail subject line (~120 chars recommended)
	 * @param message E-mail message (HTML)
	 * @param includePiada Include a little joke (in PT-BR) at the end?
	 * 
	 * @return A {@link FutureTask} that returns whether the e-mail was successfully sent or not.
	 */
	public Future<Boolean> sendFuture(final String from, final String email, final String subject, final String message, final boolean includePiada) {
		return new FutureTask<Boolean>(() -> send(from, email, subject, message, includePiada));
	}
	
	/**
	 * <p>Asynchronously sends an e-mail (via a {@link Thread} which <strong>NOT</strong> is started immediately)</p>
	 * 
	 * <p>Please, remember to invoke {@link Thread#start()} to actually send the e-mail, like this: {@code sendAsync("email@email.com", "Blah", "Blah blah").start()}</p>
	 * @param from TODO
	 * @param email E-mail address to send the e-mail to
	 * @param subject E-mail subject line (~120 chars recommended)
	 * @param message E-mail message (HTML)
	 * @param includePiada Include a little joke (in PT-BR) at the end?
	 * 
	 * @return The newly created {@link Thread}.
	 */
	public Thread sendAsync(final String from, final String email, final String subject, final String message, final boolean includePiada) {
		final Thread t = new Thread(() -> { send(from, email, subject, message, includePiada); });
		return t;
	}
	
	/**
	 * Sends an e-mail for someone.
	 * @param from TODO
	 * @param emails E-mail address to send the e-mail to
	 * @param subject E-mail subject line (~120 chars recommended)
	 * @param message E-mail message (HTML)
	 * @param includePiada Include a little joke (in PT-BR) at the end?
	 * 
	 * @return
	 */
	public boolean send(final String from, final String emails, final String subject, final String message, final boolean includePiada) {
		return send(from, parseAddresses(emails), subject, message, false);
	}
	
	/**
	 * Sends an e-mail via Responde.Ai's Amazon SES account. The e-mail uses a pre-built HTML format.
	 * @param from E-mail to fill the "from" field
	 * @param emails List of {@link InternetAddress} of the e-mails recipients
	 * @param subject The e-mail subject (~120 chars recommended)
	 * @param message The message itself
	 * @param includePiada Include a little joke (in PT-BR) at the end?
	 * 
	 * @return {@code true} if the e-mail was sent successfully
	 */
	public boolean send(final String from, final InternetAddress[] emails, final String subject, final String message, final boolean includePiada) {
		try {
			final StringBuilder html = new StringBuilder();
			final String header = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\"><html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><title>" + subject + "</title></head><body leftmargin=\"0\" marginwidth=\"0\" topmargin=\"0\" marginheight=\"0\" offset=\"0\" style=\"margin: 0;padding: 0;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;background-color: #F2F2F2;height: 100% !important;width: 100% !important;\"><center><table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" height=\"100%\" width=\"100%\" id=\"bodyTable\" style=\"border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;margin: 0;padding: 0;background-color: #F2F2F2;height: 100% !important;width: 100% !important;\"><tr><td align=\"center\" valign=\"top\" id=\"bodyCell\" style=\"mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;margin: 0;padding: 0;border-top: 0;height: 100% !important;width: 100% !important;\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><tr><td align=\"center\" valign=\"top\" style=\"mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" id=\"templatePreheader\" style=\"border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;background-color: #FFFFFF;border-top: 0;border-bottom: 0;\"><tr><td align=\"center\" valign=\"top\" style=\"mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" class=\"templateContainer\" style=\"border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><tr><td valign=\"top\" class=\"preheaderContainer\" style=\"padding-top: 9px;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><table class=\"mcnTextBlock\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><tbody class=\"mcnTextBlockOuter\"><tr><td class=\"mcnTextBlockInner\" valign=\"top\" style=\"mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><table class=\"mcnTextContentContainer\" align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"366\" style=\"border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><tbody><tr><td class=\"mcnTextContent\" style=\"padding-top: 9px;padding-left: 18px;padding-bottom: 9px;padding-right: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;color: #606060;font-family: Helvetica;font-size: 11px;line-height: 125%;text-align: left;\" valign=\"top\">" + subject + "</td></tr></tbody></table><table class=\"mcnTextContentContainer\" align=\"right\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"197\" style=\"border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><tbody><tr><td class=\"mcnTextContent\" style=\"padding-top: 9px;padding-right: 18px;padding-bottom: 9px;padding-left: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;color: #606060;font-family: Helvetica;font-size: 11px;line-height: 125%;text-align: left;\" valign=\"top\"><div style=\"text-align: right;\"><a href=\"mailto:rafael@responde.ai\" target=\"_blank\" style=\"word-wrap: break-word;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;color: #606060;font-weight: normal;text-decoration: underline;\">Dúvidas? Fale conosco =D</a></div></td></tr></tbody></table></td></tr></tbody></table></td></tr></table></td></tr></table></td></tr><tr><td align=\"center\" valign=\"top\" style=\"mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" id=\"templateHeader\" style=\"border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;background-color: #FFFFFF;border-top: 0;border-bottom: 0;\"><tr><td align=\"center\" valign=\"top\" style=\"mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" class=\"templateContainer\" style=\"border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><tr><td valign=\"top\" class=\"headerContainer\" style=\"padding-top: 10px;padding-bottom: 10px;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><table class=\"mcnDividerBlock\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><tbody class=\"mcnDividerBlockOuter\"><tr><td class=\"mcnDividerBlockInner\" style=\"padding: 0px 18px 15px;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><table style=\"border-top: 1px solid #999999;border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\" class=\"mcnDividerContent\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\"><tbody><tr><td style=\"mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><span></span></td></tr></tbody></table></td></tr></tbody></table>";
			html.append(header);
			
			final String contentBefore = "<table class=\"mcnImageBlock\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><tbody class=\"mcnImageBlockOuter\"><tr><td style=\"padding: 9px;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\" class=\"mcnImageBlockInner\" valign=\"top\"><table class=\"mcnImageContentContainer\" align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><tbody><tr><td class=\"mcnImageContent\" style=\"padding-right: 9px;padding-left: 9px;padding-top: 0;padding-bottom: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\" valign=\"top\"><img alt=\"\" src=\"https://responde-ai.s3.amazonaws.com/res/img/email-header.png\" style=\"max-width: 566px;padding-bottom: 0;display: inline !important;vertical-align: bottom;border: 0;outline: none;text-decoration: none;-ms-interpolation-mode: bicubic;\" class=\"mcnImage\" align=\"left\" width=\"564\"></td></tr></tbody></table></td></tr></tbody></table></td></tr></table></td></tr></table></td></tr><tr><td align=\"center\" valign=\"top\" style=\"mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" id=\"templateBody\" style=\"border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;background-color: #FFFFFF;border-top: 0;border-bottom: 0;\"><tr><td align=\"center\" valign=\"top\" style=\"mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" class=\"templateContainer\" style=\"border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><tr><td valign=\"top\" class=\"bodyContainer\" style=\"padding-top: 10px;padding-bottom: 10px;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><table class=\"mcnTextBlock\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><tbody class=\"mcnTextBlockOuter\"><tr><td class=\"mcnTextBlockInner\" valign=\"top\" style=\"mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><table class=\"mcnTextContentContainer\" align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><tbody><tr><td class=\"mcnTextContent\" style=\"padding-top: 9px;padding-right: 18px;padding-bottom: 9px;padding-left: 18px;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;color: #606060;font-family: Helvetica;font-size: 15px;line-height: 150%;text-align: left;\" valign=\"top\">";
			
			final String content = adaptToEmail(MARKDOWN.markdown(message));
			
			final String contentAfter = "</td></tr></tbody></table></td></tr></tbody></table>";
			html.append(contentBefore);
			html.append(content);
			html.append(contentAfter);
			
			if (includePiada) {
				final int indexPiada = (int) (Math.random() * 1000) % PERGUNTAS.length;
				final String piadaPergunta = PERGUNTAS[indexPiada];
				final String piadaResposta = RESPOSTAS[indexPiada];
				final String piada = "<table class=\"mcnTextBlock\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><tbody class=\"mcnTextBlockOuter\"><tr><td class=\"mcnTextBlockInner\" valign=\"top\" style=\"mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><table class=\"mcnTextContentContainer\" align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><tbody><tr><td class=\"mcnTextContent\" style=\"padding-top: 9px;padding-right: 18px;padding-bottom: 9px;padding-left: 18px;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;color: #606060;font-family: Helvetica;font-size: 15px;line-height: 150%;text-align: left;\" valign=\"top\">E finalmente... Pop Quiz! P: " + piadaPergunta + " &mdash; R: " + piadaResposta + "</td></tr></tbody></table></td></tr></tbody></table></td></tr></table></td></tr></table>";
				html.append(piada);
			}
			
			final String footer = "</td></tr><tr><td align=\"center\" valign=\"top\" style=\"mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" id=\"templateFooter\" style=\"border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;background-color: #F2F2F2;border-top: 0;border-bottom: 0;\"><tr><td align=\"center\" valign=\"top\" style=\"mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" class=\"templateContainer\" style=\"border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><tr><td valign=\"top\" class=\"footerContainer\" style=\"padding-top: 10px;padding-bottom: 10px;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><table class=\"mcnTextBlock\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><tbody class=\"mcnTextBlockOuter\"><tr><td class=\"mcnTextBlockInner\" valign=\"top\" style=\"mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><table class=\"mcnTextContentContainer\" align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><tbody><tr><td class=\"mcnTextContent\" style=\"padding-top: 9px;padding-right: 18px;padding-bottom: 9px;padding-left: 18px;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;color: #606060;font-family: Helvetica;font-size: 11px;line-height: 125%;text-align: left;\" valign=\"top\"><em>Copyright &copy; 2015 Responde.Ai, Todos os direitos reservados.</em></td></tr></tbody></table></td></tr></tbody></table></td></tr></table></td></tr></table></td></tr></table></td></tr></table></center></body></html>";
			html.append(footer);
			
			final Message msg = new MimeMessage(session);
			msg.setSubject(subject);
			msg.setFrom(InternetAddress.parse(from)[0]);
			msg.setRecipients(emails.length == 1 ? Message.RecipientType.TO : Message.RecipientType.BCC, emails);
			msg.setContent(html.toString(), "text/html; charset=utf-8");
			msg.setSentDate(new Date());
			
			/*
			if (!session.getTransport().isConnected()) {
				session.getTransport().connect();
			}
			session.getTransport().sendMessage(msg, emails);
			*/
			Transport.send(msg);
			
			return true;
		} catch (final Exception me) {
			me.printStackTrace();
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
	
	/**
	 * Adapt a {@link MarkdownProcessor} generated HTML to the Responde.Ai e-mail standard.
	 * @param text Processed Markdown
	 * @return Text in the Responde.Ai e-mail standard
	 */
	private String adaptToEmail(final String text) {
		return text
			.replaceAll("(?i)<h1([^>]*)>", "<h1 style=\"margin: 0;padding: 0;display: block;font-family: Helvetica;font-size: 40px;font-style: normal;font-weight: bold;line-height: 125%;letter-spacing: -1px;text-align: left;color: #333333 !important;\"$1>")
			.replaceAll("(?i)<h3([^>]*)>", "<h3 style=\"margin: 0;padding: 0;display: block;font-family: Helvetica;font-size: 18px;font-style: normal;font-weight: bold;line-height: 125%;letter-spacing: -.5px;text-align: left;color: #999999 !important;\"$1>")
			.replaceAll("(?i)<blockquote([^>]*)>\\s*<p>(.+)</p>\\s*</blockquote>\\s*", "<table class=\"mcnBoxedTextBlock\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><tbody class=\"mcnBoxedTextBlockOuter\"><tr><td class=\"mcnBoxedTextBlockInner\" valign=\"top\" style=\"mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><table class=\"mcnBoxedTextContentContainer\" align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"600\" style=\"border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><tbody><tr><td style=\"padding-top: 9px;padding-left: 18px;padding-bottom: 9px;padding-right: 18px;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\"><table style=\"border: 1px solid #BDBDBD;background-color: #F5F5F5;border-collapse: collapse;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;\" class=\"mcnTextContentContainer\" border=\"0\" cellpadding=\"18\" cellspacing=\"0\" width=\"100%\"><tbody><tr><td style=\"color: #222222;font-family: consolas,'courier new',courier,'lucida sans typewriter','lucida typewriter',monospace !important;font-size: 12px;font-weight: normal;mso-table-lspace: 0pt;mso-table-rspace: 0pt;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;line-height: 150%;text-align: center;\" class=\"mcnTextContent\" valign=\"top\"$1>$2</td></tr></tbody></table></td></tr></tbody></table></td></tr></tbody></table>")
			.replaceAll("(?i)<p([^>]*)>", "<p style=\"margin: 1em 0;padding: 0;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;color: #606060;font-family: Helvetica;font-size: 15px;line-height: 150%;text-align: left;\"$1>")
			.replaceAll("(?i)<a([^>]*)>", "<a style=\"-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;color: #15C;font-family: Helvetica\"$1>")
			.replaceAll("\\s{2,}", " ");
	}
}
