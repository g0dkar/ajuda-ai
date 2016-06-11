package ajuda.ai.website.paymentServices;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import ajuda.ai.model.billing.Payment;
import ajuda.ai.model.billing.PaymentEvent;
import ajuda.ai.model.institution.Institution;
import ajuda.ai.model.institution.InstitutionHelper;
import ajuda.ai.website.util.PersistenceService;
import br.com.caelum.vraptor.Result;

/**
 * Define ações de um Serviço de Pagamento
 * 
 * @author Rafael Lins
 *
 */
public interface PaymentProcessor {
	public static final BigDecimal HUNDRED = new BigDecimal(100);
	
	/**
	 * Cria uma ordem de pagamento de um determinado serviço de pagamentos
	 * 
	 * @param institution
	 *            A Instituição que receberá o valor
	 * @param institutionHelper
	 *            A pessoa que fez a doação
	 * @param value
	 *            Valor da Ordem de Pagamento
	 * @param name
	 *            Nome do Cliente
	 * @param email
	 *            E-mail do Cliente
	 * @param phone
	 *            Telefone do Cliente
	 * @return Um objeto {@link Payment} referente à Ordem de Pagamento
	 */
	default Payment createPayment(final Institution institution, final InstitutionHelper institutionHelper,
			final int value, final String name, final String email, final String phone) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Cria uma ordem de pagamento de um determinado serviço de pagamentos
	 * 
	 * @param result
	 *            Um {@link PaymentEvent}. Caso essa função <strong>NÃO</strong> retorne
	 *            {@code null} espera-se que ela tenha utilizado o {@code result} para enviar como
	 *            resposta o que o serviço de pagamento espera como resposta da notificação
	 */
	default PaymentEvent processEvent(final Institution institution, final HttpServletRequest request,
			final PersistenceService ps, final Result result) throws Exception {
		throw new UnsupportedOperationException();
	}
}
