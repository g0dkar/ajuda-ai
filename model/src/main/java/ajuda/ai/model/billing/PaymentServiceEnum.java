package ajuda.ai.model.billing;

import java.util.HashMap;
import java.util.Map;

import ajuda.ai.util.JsonUtils;

/**
 * Serviços de Pagamento suportados. Uma {@link Enum} é usada pois é a forma mais legal de se fazer Singletons. Créditos da Técnica: Daniel Mário
 * 
 * @author Rafael Lins
 * @author Daniel Mário
 */
public enum PaymentServiceEnum {
	MOIP {
		public String status(final int status) {
			switch (status) {
				case 1: return "Autorizado";
				case 2: return "Iniciado";
				case 3: return "Boleto Impresso";
				case 4: return "Concluído";
				case 5: return "Cancelado";
				case 6: return "Em Análise";
				case 7: return "Estornado";
				default: return "Desconhecido";
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
	};
	
//	PAG_SEGURO {
//		public String status(final int status) {
//			switch (status) {
//				case 1: return "Aguardando Pagamento";
//				case 2: return "Em Análise";
//				case 3: return "Paga";
//				case 4: return "Paga";
//				case 5: return "Em Disputa";
//				case 6: return "Devolvida";
//				case 7: return "Cancelada";
//				case 8: return "Devolvida";
//				case 9: return "Pagamento Contestado";
//				default: return "Desconhecido";
//			}
//		}
//
//		public boolean isPaid(final int status) {
//			return status == 3 || status == 4;
//		}
//
//		public boolean isReadyForAccounting(final int status) {
//			return status == 4;
//		}
//
//		public boolean isCancelled(final int status) {
//			return status == 6 || status == 7 || status == 8;
//		}
//
//		public String getThanksTransactionIdParameter() {
//			return "transaction_id";
//		}
//
//		public String extractPaymentServiceData(final String data) {
//			final Map<String, String> paymentServiceData = new HashMap<>(1);
//			paymentServiceData.put("email", data);
//			return JsonUtils.toJson(paymentServiceData);
//		}
//	};
	
	public abstract String status(int status);
	public abstract boolean isPaid(int status);
	public abstract boolean isReadyForAccounting(int status);
	public abstract boolean isCancelled(int status);
	public abstract String getThanksTransactionIdParameter();
	public abstract String extractPaymentServiceData(String data);
}
