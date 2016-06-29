package ajuda.ai.website.paymentServices;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import ajuda.ai.model.billing.Payment;
import ajuda.ai.model.billing.PaymentEvent;
import ajuda.ai.model.billing.PaymentServiceEnum;
import ajuda.ai.model.institution.Institution;
import ajuda.ai.model.institution.InstitutionHelper;
import ajuda.ai.util.StringUtils;
import ajuda.ai.website.PostRedirectController;
import ajuda.ai.website.util.PersistenceService;
import br.com.caelum.vraptor.Result;

/**
 * Executa processamento de pagamentos do <a href="https://moip.com.br/">MoIP</a>
 * @author Rafael Lins
 *
 */
public class MoipPaymentProcessor implements PaymentProcessor {
	public Payment createPayment(final Institution institution, final InstitutionHelper institutionHelper, final int value, final PersistenceService ps, final Result result, final Logger log) {
		final Map<String, String> paymentServiceData = institution.getPaymentServiceDataMap();
		
		final Payment payment = new Payment();
		payment.setInstitution(institution);
		payment.setInstitutionHelper(institutionHelper);
		payment.setPaymentServiceId(null);
		payment.setDescription("Doação MoIP");
		payment.setPaymentService(institution.getPaymentService());
		payment.setTimestamp(new Date());
		payment.setValue(value);
		payment.setPaid(false);
		payment.setCancelled(false);
		payment.setReadyForAccounting(false);
		ps.persist(payment);
		
		final HashMap<String, Object> params = new HashMap<>(8);
		params.put("id_carteira", paymentServiceData.get("email"));
		params.put("valor", value);
		params.put("nome", maxSize("Doação: " + institution.getName(), 64));
		params.put("descricao", "Doação através do Projeto Ajuda.Ai");
		params.put("id_transacao", payment.getId());
		params.put("pagador_nome", maxSize(institutionHelper.getName(), 90));
		params.put("pagador_email", maxSize(institutionHelper.getEmail(), 45));
		
		result.redirectTo(PostRedirectController.class).postRedirect("https://www.moip.com.br/PagamentoMoIP.do", params, "ISO-8859-1");
		return payment;
	}
	
	public PaymentEvent processEvent(final Institution institution, final HttpServletRequest request, final PersistenceService ps, final Result result, final Logger log) throws Exception {
		if (request.getMethod().equals("POST")) {
			final String idPayment = request.getParameter("id_transacao");
			final String idMoip = request.getParameter("cod_moip");
			final String email = request.getParameter("email_consumidor");
			final int valor = StringUtils.parseInteger(request.getParameter("valor"), 0);
			final int status = StringUtils.parseInteger(request.getParameter("status_pagamento"), -1);
			Payment payment = ps.find(Payment.class, StringUtils.parseLong(idPayment, 0));
			InstitutionHelper helper = (InstitutionHelper) ps.createQuery("FROM InstitutionHelper WHERE institution = :institution AND LOWER(email) = LOWER(:email)").setParameter("institution", institution).setParameter("email", email).getSingleResult();
			
			if (helper == null) {
				helper = new InstitutionHelper();
				helper.setInstitution(institution);
				helper.setEmail(email);
				helper.setPaymentEmail(email);
				helper.setName("Anônimo");
				helper.setTimestamp(new Date());
				ps.persist(helper);
			}
			else {
				helper.setPaymentEmail(email);
				helper = ps.merge(helper);
			}
			
			if (payment != null) {
				if (!payment.getInstitutionHelper().getId().equals(helper.getId())) {
					payment.setInstitutionHelper(helper);
				}
			}
			else {
				payment = new Payment();
				payment.setInstitution(institution);
				payment.setInstitutionHelper(helper);
				payment.setPaymentServiceId(idMoip);
				payment.setDescription("Pagamento MoIP");
				payment.setPaymentService(PaymentServiceEnum.MOIP);
				payment.setTimestamp(new Date());
				payment.setValue(valor);
				payment.setPaid(false);
				payment.setCancelled(false);
				payment.setReadyForAccounting(false);
				ps.persist(payment);
			}
			
			ps.createQuery("UPDATE InstitutionHelper SET lastPayment = :payment WHERE id = :id").setParameter("payment", payment).setParameter("id", helper.getId()).executeUpdate();
			
			final PaymentEvent newEvent = new PaymentEvent();
			newEvent.setCurrency("BRL");	// Apenas BRL é suportado pelo MoIP
			newEvent.setPayment(payment);
			newEvent.setPaymentType(request.getParameter("forma_pagamento"));
			newEvent.setPaymentTypeInfo(request.getParameter("tipo_pagamento"));
			newEvent.setStatus(status);
			newEvent.setTimestamp(new Date());
			newEvent.setTransactionServiceId(idMoip);
			newEvent.setValue(valor);
			ps.persist(newEvent);
			
			final Query updatePayment = ps.createQuery("UPDATE Payment SET paid = :paid, cancelled = :cancelled, readyForAccounting = :readyForAccounting WHERE id = :id").setParameter("id", payment.getId());
			updatePayment.setParameter("paid", PaymentServiceEnum.MOIP.isPaid(newEvent.getStatus()));
			updatePayment.setParameter("cancelled", PaymentServiceEnum.MOIP.isCancelled(newEvent.getStatus()));
			updatePayment.setParameter("readyForAccounting", PaymentServiceEnum.MOIP.isReadyForAccounting(newEvent.getStatus()));
			updatePayment.executeUpdate();
			
			result.nothing();
			return newEvent;
		}
		
		return null;
	}
	
	private String maxSize(final String string, final int size) {
		return string.length() <= size ? string : string.substring(0, size + 1);
	}
}
