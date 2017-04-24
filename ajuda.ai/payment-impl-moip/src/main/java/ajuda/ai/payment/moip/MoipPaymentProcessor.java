package ajuda.ai.payment.moip;

import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import ajuda.ai.model.billing.Payment;
import ajuda.ai.model.billing.PaymentEvent;
import ajuda.ai.model.institution.Institution;
import ajuda.ai.model.user.User;
import ajuda.ai.payment.PaymentGateway;
import ajuda.ai.payment.annotation.PaymentService;
import br.com.caelum.vraptor.Result;

/**
 * Executa processamento de pagamentos do <a href="https://moip.com.br/">MoIP</a>
 * @author Rafael Lins
 *
 */
@PaymentService("moip")
public class MoipPaymentProcessor implements PaymentGateway {
	public Payment createPayment(final Institution institution, final User helper, final int value, final boolean addCosts, final int paymentType, final Result result, final Logger log) {
		final Payment payment = new Payment();
		payment.setInstitution(institution);
		payment.setHelper(helper);
		payment.setPaymentServiceId(null);
		payment.setDescription("Doação MoIP");
		payment.setPaymentService(institution.getPaymentService());
		payment.setTimestamp(new Date());
		payment.setValue(value);
//		payment.setRealValue(addCosts ? PaymentServiceEnum.MOIP.valuePlusTariffs(value, paymentType) : value);
		payment.setPaid(false);
		payment.setCancelled(false);
		payment.setReadyForAccounting(false);
		payment.setPayeeName((helper.getFirstname() + " " + helper.getLastname()).trim());
		payment.setPayeeEmail(helper.getEmail());
		
		final HashMap<String, Object> params = new HashMap<>(8);
		params.put("id_carteira", institution.getAttributes().get("moip_email"));
		params.put("valor", payment.getRealValue());
		params.put("nome", maxSize("Doação: " + institution.getName(), 64));
		params.put("descricao", "Doação através do Projeto Ajuda.Ai");
		params.put("id_transacao", payment.getId());
//		params.put("pagador_nome", maxSize(helper.isAnonymous() || StringUtils.isBlank(helper.getName()) ? "Anônimo" : helper.getName(), 90));
		params.put("pagador_email", maxSize(helper.getEmail(), 45));
		
//		result.redirectTo(PostRedirectController.class).postRedirect("https://www.moip.com.br/PagamentoMoIP.do", params, "ISO-8859-1");
		return payment;
	}
	
	public PaymentEvent processEvent(final Institution institution, final HttpServletRequest request, final Result result, final Logger log) throws Exception {
		if (request.getMethod().equals("POST")) {
			final String idPayment = request.getParameter("id_transacao");
//			final Payment payment = ps.find(Payment.class, idPayment);
			
//			if (payment != null) {
//				final String idMoip = request.getParameter("cod_moip");
//				final int valor = StringUtils.parseInteger(request.getParameter("valor"), 0);
//				final int status = StringUtils.parseInteger(request.getParameter("status_pagamento"), -1);
//				
//				final PaymentEvent newEvent = new PaymentEvent();
//				newEvent.setCurrency("BRL");	// Apenas BRL é suportado pelo MoIP
//				newEvent.setPayment(payment);
//				newEvent.setPaymentType(request.getParameter("forma_pagamento"));
//				newEvent.setPaymentTypeInfo(request.getParameter("tipo_pagamento"));
//				newEvent.setStatus(status);
//				newEvent.setTimestamp(new Date());
//				newEvent.setTransactionServiceId(idMoip);
//				newEvent.setValue(valor);
//				ps.persist(newEvent);
//				
//				final Query updatePayment = ps.createQuery("UPDATE Payment SET paid = :paid, cancelled = :cancelled, readyForAccounting = :readyForAccounting WHERE id = :id").setParameter("id", payment.getId());
//				updatePayment.setParameter("paid", PaymentServiceEnum.MOIP.isPaid(newEvent.getStatus()));
//				updatePayment.setParameter("cancelled", PaymentServiceEnum.MOIP.isCancelled(newEvent.getStatus()));
//				updatePayment.setParameter("readyForAccounting", PaymentServiceEnum.MOIP.isReadyForAccounting(newEvent.getStatus()));
//				updatePayment.executeUpdate();
//				
//				result.nothing();
//				return newEvent;
//			}
//			else {
//				result.nothing();
//				return null;
//			}
		}
		
		return null;
	}
	
	private String maxSize(final String string, final int size) {
		return string.length() <= size ? string : string.substring(0, size + 1);
	}
}
