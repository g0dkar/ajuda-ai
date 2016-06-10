package ajuda.ai.website.paymentServices;

import java.math.BigDecimal;

import ajuda.ai.model.billing.Payment;
import ajuda.ai.model.institution.Institution;
import ajuda.ai.model.institution.InstitutionHelper;

/**
 * Define ações de um Serviço de Pagamento
 * @author Rafael Lins
 *
 */
public interface PaymentService {
	public static final BigDecimal HUNDRED = new BigDecimal(100);
	
	/**
	 * Cria uma ordem de pagamento de um determinado serviço de pagamentos
	 * @param institution TODO
	 * @param institutionHelper TODO
	 * @param value Valor da Ordem de Pagamento
	 * @param name Nome do Cliente
	 * @param email E-mail do Cliente
	 * @param phone Telefone do Cliente
	 * @return Um objeto {@link Payment} referente à Ordem de Pagamento
	 */
	Payment createPayment(final Institution institution, final InstitutionHelper institutionHelper, final int value, final String name, final String email, final String phone);
}
