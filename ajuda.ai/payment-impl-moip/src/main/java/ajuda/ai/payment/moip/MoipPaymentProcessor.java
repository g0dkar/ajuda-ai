package ajuda.ai.payment.moip;

import java.util.Date;
import java.util.HashMap;

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
@PaymentServiceImpl("moip")
public class MoipPaymentProcessor implements PaymentGateway {
	// Retirado de: https://labs.moip.com.br/parametro/statuspagamento/
	private static final int STATUS_PAGO = 1;
	private static final int STATUS_INICIADO = 2;
	private static final int STATUS_BOLETO_IMPRESSO = 3;
	private static final int STATUS_CONCLUIDO = 4;
	private static final int STATUS_CANCELADO = 5;
	private static final int STATUS_EM_ANALISE = 6;
	private static final int STATUS_ESTORNADO = 7;
	private static final int STATUS_REEMBOLSADO = 9;
	
	private static final String MOIP_PAYMENT_ACTION_URL = "https://www.moip.com.br/PagamentoMoIP.do";
//	private static final String MOIP_PAYMENT_ACTION_URL = "https://desenvolvedor.moip.com.br/sandbox/PagamentoMoIP.do";
	
	@Inject
	private Logger log;
	
	@Inject
	private PaymentPersistence pp;
	
	public int calculateCosts(final int value, final int paymentType) {
		return (int)(Math.ceil((value + 65) / (paymentType == 0 ? .9451 : .9651)) - value);
	}
	
	@Override
	public Payment createPayment(final Institution institution, final boolean anonymous, final String payeeName, final String payeeEmail, final int value, final boolean addCosts, final int paymentType) {
		if (log.isDebugEnabled()) {
			log.debug("Criando Payment para MoIP: Instituição #{}, Anonymous? {}, Valor: {}", institution.getId(), anonymous, value);
		}
		
		final Payment payment = new Payment();
		payment.setInstitution(institution);
		payment.setPaymentServiceId(null);
		payment.setDescription("Doação MoIP");
		payment.setPaymentService(institution.getPaymentService());
		payment.setTimestamp(new Date());
		payment.setValue(value);
		payment.setRealValue(value + (addCosts ? calculateCosts(value, paymentType) : 0));
		payment.setPaid(false);
		payment.setCancelled(false);
		payment.setReadyForAccounting(false);
		payment.setAnonymous(anonymous);
		payment.setPayeeName(payeeName);
		payment.setPayeeEmail(payeeEmail);
		payment.generateUUID();
		
		return payment;
	}
	
	@Override
	public void redirectToPayment(final Payment payment, final Result result) {
		final Institution institution = payment.getInstitution();
		
		final HashMap<String, Object> params = new HashMap<>(8);
		params.put("id_carteira", institution.getAttributes().get("moip_email"));
		params.put("valor", payment.getRealValue());
		params.put("nome", maxSize("Doação: " + institution.getName(), 64));
		params.put("descricao", "Doação através do Projeto Ajuda.Ai");
		params.put("id_transacao", payment.getId());
		params.put("pagador_nome", maxSize(payment.isAnonymous() ? "Anonimo" : payment.getPayeeName(), 90));
		params.put("pagador_email", maxSize(payment.getPayeeEmail(), 45));
		
		result.include("action", MOIP_PAYMENT_ACTION_URL);
		result.include("paramMap", params);
		result.include("charset", "ISO-8859-1");
		
		if (log.isDebugEnabled()) {
			log.debug("Redirecionando para MoIP (action = {}) com os parâmetros: {}", MOIP_PAYMENT_ACTION_URL, params);
		}
	}
	
	@Override
	public PaymentEvent processEvent(final HttpServletRequest request, final Result result, Institution institution) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Recebendo notificação de transação do MoIP. Checando método HTTP (deve ser POST)");
		}
		
		if (request.getMethod().equals("POST")) {
			if (log.isDebugEnabled()) {
				log.debug("É um HTTP POST. Buscando Payment no banco. Parâmetros do Request: {}", request.getParameterMap());
			}
			
			final Long idPayment = StringUtils.parseLong(request.getParameter("id_transacao"), 0);
			final Payment payment = pp.get(idPayment);
			
			if (payment != null) {
				if (log.isDebugEnabled()) {
					log.debug("Payment ID {} encontrado. Continuando...", payment.getId());
				}
				
				final String idMoip = request.getParameter("cod_moip");
				final int valor = StringUtils.parseInteger(request.getParameter("valor"), 0);
				final int status = StringUtils.parseInteger(request.getParameter("status_pagamento"), -1);
				
				final PaymentEvent newEvent = new PaymentEvent();
				newEvent.setCurrency("BRL");	// Apenas BRL é suportado pelo MoIP
				newEvent.setPayment(payment);
				newEvent.setPaymentType(request.getParameter("forma_pagamento"));
				newEvent.setPaymentTypeInfo(request.getParameter("tipo_pagamento"));
				newEvent.setStatus(status);
				newEvent.setTimestamp(new Date());
				newEvent.setTransactionServiceId(idMoip);
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
		return status != STATUS_INICIADO;
	}
	
	private boolean isCancelado(final int status) {
		return status == STATUS_CANCELADO || status == STATUS_ESTORNADO || status == STATUS_REEMBOLSADO;
	}
	
	private boolean isConcluido(final int status) {
		return status == STATUS_CONCLUIDO;
	}
}
