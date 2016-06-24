package ajuda.ai.model.billing;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import ajuda.ai.util.JsonUtils;

/**
 * Serviços de Pagamento suportados. Uma {@link Enum} é usada pois é a forma mais legal de se fazer
 * Singletons. Créditos da Técnica: Daniel Mário
 * 
 * @author Rafael Lins
 * @author Daniel Mário
 */
public enum PaymentServiceEnum {
	MOIP {
		public String status(final int status) {
			switch (status) {
				case 1:
					return "Autorizado";
				case 2:
					return "Iniciado";
				case 3:
					return "Boleto Impresso";
				case 4:
					return "Concluído";
				case 5:
					return "Cancelado";
				case 6:
					return "Em Análise";
				case 7:
					return "Estornado";
				default:
					return "Desconhecido";
			}
		}
		
		public boolean isPaid(final int status) {
			return status == 1 || status == 4;
		}
		
		public boolean isReadyForAccounting(final int status) {
			return status == 4;
		}
		
		public boolean isCancelled(final int status) {
			return status == 5 || status == 7;
		}
		
		public String getThanksTransactionIdParameter() {
			return "_tid";
		}
		
		public String extractPaymentServiceData(final String data) {
			final Map<String, String> paymentServiceData = new HashMap<>(1);
			paymentServiceData.put("email", data);
			return JsonUtils.toJson(paymentServiceData);
		}
		
		/**
		 * MoIP cobra 5.49% de tarifa sob o valor da transação com cartão de crédito. O valor de
		 * 94.51% foi calculado pelo Wolfram Alpha:
		 * <a href="http://www.wolframalpha.com/input/?i=evaluate+X+-+(0.0549X+%2B+0.65)+%3D+10">
		 * http://www.wolframalpha.com/input/?i=evaluate+X+-+(0.0549X+%2B+0.65)+%3D+10</a>
		 */
		private final BigDecimal CARD_TARIFF = new BigDecimal("0.9451");
		
		/**
		 * MoIP cobra 3.49% de tarifa sob o valor da transação com boleto/débito. O valor de 96.51%
		 * foi calculado pelo Wolfram Alpha:
		 * <a href="http://www.wolframalpha.com/input/?i=evaluate+X+-+(0.0349X+%2B+0.65)+%3D+10">
		 * http://www.wolframalpha.com/input/?i=evaluate+X+-+(0.0349X+%2B+0.65)+%3D+10</a>
		 */
		private final BigDecimal BILLET_TARIFF = new BigDecimal("0.9651");
		
		/**
		 * MoIP sempre adiciona R$ 0,65 em cada transação
		 */
		private final int CONSTANT_TARIFF = 65;
		
		/**
		 * Fórmula para cálculo do valor final:
		 */
		public int valuePlusTariffs(final int value, final int paymentType) {
			switch (paymentType) {
				case 0: return new BigDecimal(value + CONSTANT_TARIFF).divide(CARD_TARIFF, 0, RoundingMode.CEILING).intValue();
				case 1: return new BigDecimal(value + CONSTANT_TARIFF).divide(BILLET_TARIFF, 0, RoundingMode.CEILING).intValue();
				default: return value;
			}
		}
	};
	
	// PAG_SEGURO {
	// public String status(final int status) {
	// switch (status) {
	// case 1: return "Aguardando Pagamento";
	// case 2: return "Em Análise";
	// case 3: return "Paga";
	// case 4: return "Paga";
	// case 5: return "Em Disputa";
	// case 6: return "Devolvida";
	// case 7: return "Cancelada";
	// case 8: return "Devolvida";
	// case 9: return "Pagamento Contestado";
	// default: return "Desconhecido";
	// }
	// }
	//
	// public boolean isPaid(final int status) {
	// return status == 3 || status == 4;
	// }
	//
	// public boolean isReadyForAccounting(final int status) {
	// return status == 4;
	// }
	//
	// public boolean isCancelled(final int status) {
	// return status == 6 || status == 7 || status == 8;
	// }
	//
	// public String getThanksTransactionIdParameter() {
	// return "transaction_id";
	// }
	//
	// public String extractPaymentServiceData(final String data) {
	// final Map<String, String> paymentServiceData = new HashMap<>(1);
	// paymentServiceData.put("email", data);
	// return JsonUtils.toJson(paymentServiceData);
	// }
	// };
	
	public abstract String status(final int status);
	public abstract boolean isPaid(final int status);
	public abstract boolean isReadyForAccounting(final int status);
	public abstract boolean isCancelled(final int status);
	public abstract String getThanksTransactionIdParameter();
	public abstract String extractPaymentServiceData(final String data);
	public abstract int valuePlusTariffs(final int value, final int paymentType);
}
