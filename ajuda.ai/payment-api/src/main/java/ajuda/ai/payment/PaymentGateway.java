package ajuda.ai.payment;

import javax.servlet.http.HttpServletRequest;

import ajuda.ai.model.billing.Payment;
import ajuda.ai.model.billing.PaymentEvent;
import ajuda.ai.model.institution.Institution;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.HttpResult;
import br.com.caelum.vraptor.view.Results;

/**
 * Define um Serviço de Pagamento
 * 
 * @author Rafael Lins
 *
 */
public interface PaymentGateway {
	/**
	 * Cria uma ordem de pagamento de um determinado serviço de pagamentos. Espera-se que a classe
	 * que implementa esta interface faça as requisições HTTP necessárias junto ao serviço de
	 * pagamento sendo implementado e utilize o {@link Result} para responder corretamente a
	 * requisição (vulgo "redirecionar para o ambiente de pagamento").
	 * 
	 * @param institution
	 *            A Instituição que receberá o valor
	 * @param anonymous
	 *            A pessoa que fez a doação
	 * @param payeeName TODO
	 * @param payeeEmail TODO
	 * @param value
	 *            Valor da Ordem de Pagamento ({@code 10000} = {@code $100.00}, para evitar erros de
	 *            arredondamento)
	 * @param addCosts
	 *            Adicionar os custos da transação ao valor?
	 * @param paymentType
	 *            Caso {@code addCosts} seja true, que tipo de operação levar em consideração?
	 *            (normalmente pagamentos em cartão tem taxas maiores, por exemplo)
	 * @return Objeto {@link Payment} referente a esse pagamento.
	 */
	default Payment createPayment(final Institution institution, final boolean anonymous, final String payeeName, final String payeeEmail, final int value, final boolean addCosts, final int paymentType) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Redireciona o usuário para a página para o pagamento de um {@link Payment}
	 * @param payment
	 * @param result
	 */
	default void redirectToPayment(final Payment payment, final Result result) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Processa uma notificação de pagamento enviada por um serviço de pagamento. Espera-se que a
	 * classe que implementa essa interface utilize o {@link Result} para responder corretamente a
	 * requisição. Caso aconteça algum erro (lançamento de Exception) ou esse método retorne
	 * {@code null} um erro 500 será automaticamente enviado como resposta.
	 * 
	 * Dica: {@link Result#nothing() result.nothing()} envia um HTTP 200 em branco como resposta.
	 * @param request
	 *            O {@link HttpServletRequest request} enviado (verificação se é {@code GET} ou
	 *            {@code POST} deve ser feita {@link HttpServletRequest#getMethod() manualmente})
	 * @param result
	 *            {@link Result} para se configurar a resposta.
	 * 
	 * @return Um {@link PaymentEvent}. Caso essa função <strong>NÃO</strong> retorne {@code null}
	 *         espera-se que ela tenha utilizado o {@code result} para enviar como resposta o que o
	 *         serviço de pagamento espera como resposta da notificação (se for apenas um HTTP 200
	 *         recomenda-se chamar {@link Result#nothing() result.nothing()}, se precisar enviar
	 *         algo, tente {@code result.use(Results.http()).body("O que você quiser")})
	 * @see HttpServletRequest
	 * @see HttpServletRequest#getMethod()
	 * @see Result
	 * @see Results#http()
	 * @see HttpResult#body(String)
	 */
	default PaymentEvent processEvent(final HttpServletRequest request, final Result result) throws Exception {
		throw new UnsupportedOperationException();
	}
}
