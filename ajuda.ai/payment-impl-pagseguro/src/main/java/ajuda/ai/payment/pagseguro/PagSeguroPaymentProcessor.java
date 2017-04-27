package ajuda.ai.payment.pagseguro;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import ajuda.ai.model.billing.Payment;
import ajuda.ai.model.billing.PaymentEvent;
import ajuda.ai.model.institution.Institution;
import ajuda.ai.payment.PaymentGateway;
import ajuda.ai.payment.annotation.PaymentServiceImpl;
import ajuda.ai.persistence.model.billing.PaymentPersistence;
import ajuda.ai.util.StringUtils;
import br.com.caelum.vraptor.Result;

/**
 * Executa processamento de pagamentos do <a href="https://moip.com.br/">MoIP</a>
 * @author Rafael Lins
 *
 */
@PaymentServiceImpl("pagseguro")
public class PagSeguroPaymentProcessor implements PaymentGateway {
	// Retirado de: https://dev.pagseguro.uol.com.br/documentacao/notificacoes/api-de-notificacoes
	private static final int STATUS_AGUARDANDO = 1;
	private static final int STATUS_EM_ANALISE = 2;
	private static final int STATUS_PAGO = 3;
	private static final int STATUS_DISPONIVEL = 4;
	private static final int STATUS_EM_DISPUTA = 5;
	private static final int STATUS_DEVOLVIDA = 6;
	private static final int STATUS_CANCELADA = 7;
	private static final int STATUS_DEBITADO = 8;
	private static final int STATUS_RETENCAO_TEMP = 9;
	
	private static final String PAGSEGURO_ENDPOINT = "https://ws.sandbox.pagseguro.uol.com.br";
//	private static final String PAGSEGURO_ENDPOINT = "https://ws.pagseguro.uol.com.br";
	
	@Inject
	private Logger log;
	
	@Inject
	private HttpServletRequest request;
	
	@Inject
	private PaymentPersistence pp;
	
	
	public int calculateCosts(final int value) {
		return (int)(Math.ceil((value + 65) / .9451) - value);
	}
	
	@Override
	public Payment createPayment(final Institution institution, final boolean anonymous, final String payeeName, final String payeeEmail, final int value, final boolean addCosts, final int paymentType) {
		if (log.isDebugEnabled()) {
			log.debug("Criando Payment para PagSeguro: Instituição #{}, Anonymous? {}, Valor: {}", institution.getId(), anonymous, value);
		}
		
		final Payment payment = new Payment();
		payment.setInstitution(institution);
		payment.setPaymentServiceId(null);
		payment.setDescription("Doação PagSeguro");
		payment.setPaymentService(institution.getPaymentService());
		payment.setTimestamp(new Date());
		payment.setValue(value);
		payment.setRealValue(value + (addCosts ? calculateCosts(value) : 0));
		payment.setPaid(false);
		payment.setCancelled(false);
		payment.setReadyForAccounting(false);
		payment.setAnonymous(anonymous);
		payment.setPayeeName(payeeName);
		payment.setPayeeEmail(payeeEmail);
		payment.generateUUID();
		
		final String requestCode = requestCode(payment);
		if (requestCode != null) {
			payment.setPaymentServiceId(requestCode);
			return payment;
		}
		else {
			return null;
		}
	}
	
	@Override
	public void redirectToPayment(final Payment payment, final Result result) {
		final String redirectUrl = PAGSEGURO_ENDPOINT + "/v2/checkout/payment.html?code=" + payment.getPaymentServiceId();
		
		if (log.isDebugEnabled()) {
			log.debug("Redirecionando para PagSeguro: {}", redirectUrl);
		}
		
		result.redirectTo(redirectUrl);
	}
	
	@Override
	public PaymentEvent processEvent(final HttpServletRequest request, final Result result, final Institution institution) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Recebendo notificação de transação do PagSeguro. Checando método HTTP (deve ser POST)");
		}
		
		// https://ws.sandbox.pagseguro.uol.com.br/v3/transactions/NOTIFICATION_CODE?email=suporte%40lojamodelo.com.br&token=57BE455F4EC148E5A54D9BB91C5AC12C
		
		if (request.getMethod().equals("POST")) {
			if (log.isDebugEnabled()) {
				log.debug("É um HTTP POST. Buscando Payment junto ao PagSeguro. Parâmetros do Request: {}", request.getParameterMap());
			}
			
//			notificationCode=766B9C-AD4B044B04DA-77742F5FA653-E1AB24
//			notificationType=transaction
			
			final String notificationCode = request.getParameter("notificationCode");
			final String transactionRequest = PAGSEGURO_ENDPOINT + "/v3/transactions/" + notificationCode + "?email=" + StringUtils.encodeURLComponent(institution.getAttributes().get("pagseguro_email")) + "&token=" + institution.getAttributes().get("pagseguro_token");
			final String transactionData = httpGet(transactionRequest);
			
			final Long idPayment = StringUtils.parseLong(extractTagValue("reference", transactionData), 0);
			final Payment payment = pp.get(idPayment);
			
			if (payment != null) {
				if (log.isDebugEnabled()) {
					log.debug("Payment ID {} encontrado. Continuando...", payment.getId());
				}
				
				final String idPagSeguro = extractTagValue("code", transactionData);
				final int valor = (int)(StringUtils.parseDouble(extractTagValue("netAmount", transactionData), 0) * 100);
				final int status = StringUtils.parseInteger(extractTagValue("status", transactionData), -1);
				
				final PaymentEvent newEvent = new PaymentEvent();
				newEvent.setCurrency("BRL");	// Apenas BRL é suportado pelo PagSeguro
				newEvent.setPayment(payment);
				newEvent.setPaymentType(extractTagValue("type", transactionData));
				newEvent.setPaymentTypeInfo(extractTagValue("code", transactionData));
				newEvent.setStatus(status);
				newEvent.setTimestamp(new Date());
				newEvent.setTransactionServiceId(idPagSeguro);
				newEvent.setValue(valor);
				pp.persist(newEvent);
				
				if (log.isDebugEnabled()) {
					log.debug("Atualizando status dos campos paid, cancelled e readyForAccounting do Payment #{}", payment.getId());
				}
				
				final Query updatePayment = pp.query("UPDATE Payment SET paid = :paid, cancelled = :cancelled, readyForAccounting = :readyForAccounting WHERE id = :id").setParameter("id", payment.getId());
				updatePayment.setParameter("paid", isPago(newEvent.getStatus()));
				updatePayment.setParameter("cancelled", isCancelado(newEvent.getStatus()));
				updatePayment.setParameter("readyForAccounting", isConcluido(newEvent.getStatus()));
				updatePayment.executeUpdate();
				
				if (log.isDebugEnabled()) {
					log.debug("Atualizados para: paid = {}, cancelled = {} e readyForAccounting = {} - Payment #{}", isPago(newEvent.getStatus()), isCancelado(newEvent.getStatus()), isConcluido(newEvent.getStatus()), payment.getId());
				}
				
				result.nothing();
				return newEvent;
			}
		}
		
		result.nothing();
		return null;
	}
	
	private String maxSize(final String string, final int size) {
		return string.length() <= size ? string : string.substring(0, size + 1);
	}
	
	private boolean isPago(final int status) {
		return status == STATUS_PAGO || status == STATUS_EM_ANALISE || status == STATUS_EM_DISPUTA || status == STATUS_RETENCAO_TEMP || status == STATUS_DISPONIVEL;
	}
	
	private boolean isCancelado(final int status) {
		return status == STATUS_DEVOLVIDA || status == STATUS_DEBITADO || status == STATUS_CANCELADA;
	}
	
	private boolean isConcluido(final int status) {
		return status == STATUS_DISPONIVEL;
	}
	
	/**
	 * Executa a requisição para registro de checkout e retorna o código do mesmo, dado como resposta da requisição
	 * @param payment {@link Payment} que gerou essa requisição
	 * @return Código da requisição, se der certo
	 */
	private String requestCode(final Payment payment) {
		final StringBuilder xml = new StringBuilder("<?xml version=\"1.0\"?><checkout>");
		
		xml.append("<sender>");
		xml.append("<name>");
		xml.append(maxSize(StringUtils.stripHTML(payment.getPayeeName()), 50));
		xml.append("</name>");
		xml.append("<email>");
		xml.append(maxSize(StringUtils.stripHTML(payment.getPayeeEmail()), 60));
		xml.append("</email>");
		xml.append("<ip>");
		xml.append(request.getRemoteAddr());
		xml.append("</ip>");
		xml.append("</sender>");
		
		xml.append("<currency>BRL</currency>");
		
		xml.append("<items>");
		xml.append("<item>");
		xml.append("<id>1</id>");
		xml.append("<description>Doação: ");
		xml.append(maxSize(StringUtils.stripHTML(payment.getInstitution().getName()), 92));
		xml.append("</description>");
		xml.append("<amount>");
		xml.append(payment.getRealValue() / 100.0);
		xml.append("</amount>");
		xml.append("<quantity>1</quantity>");
		xml.append("</item>");
		xml.append("</items>");
		
		xml.append("<reference>");
		xml.append(payment.getId());
		xml.append("</reference>");
		
		xml.append("</checkout>");
		
		
		try {
			final String resp = httpPost(PAGSEGURO_ENDPOINT + "/v2/checkout?email=" + StringUtils.encodeURLComponent(payment.getInstitution().getAttributes().get("pagseguro_email")) + "&token=" + payment.getInstitution().getAttributes().get("pagseguro_token"), xml.toString());
			return resp != null ? extractTagValue("code", resp) : null;
		} catch (final IOException e) {
			log.error("Erro ao ler/escrever dados do PagSeguro para criar cobrança", e);
		}
		
		return null;
	}
	
	private String httpPost(final String postUrl, final String data) throws IOException {
		final URL url = new URL(postUrl);
		final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "application/xml; charset=ISO-8859-1");
		conn.getOutputStream().write(data.getBytes("ISO-8895-1"));
		
		if (conn.getResponseCode() == 200) {
			final StringBuilder resp = new StringBuilder();
			final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "ISO-8859-1"));
			String line;
			
			while ((line = br.readLine()) != null) {
				resp.append(line);
			}
			
			return resp.toString();
		}
		
		return null;
	}
	
	private String httpGet(final String postUrl) throws IOException {
		final URL url = new URL(postUrl);
		final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("Content-Type", "application/xml; charset=ISO-8859-1");
		
		if (conn.getResponseCode() == 200) {
			final StringBuilder resp = new StringBuilder();
			final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "ISO-8859-1"));
			String line;
			
			while ((line = br.readLine()) != null) {
				resp.append(line);
			}
			
			return resp.toString();
		}
		
		return null;
	}
	
	private String extractTagValue(final String tag, final String string) {
		final Matcher matcher = Pattern.compile("(?i)<" + tag + ">([^<]+)</"+ tag + ">").matcher(string);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}
}
