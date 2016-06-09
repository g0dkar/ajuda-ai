package ajuda.ai.model.billing;

public enum PaymentServiceEnum {
	MOIP {
		public int status(final String status) {
			return 0;
		}
		
		public String statusDescription(final int status) {
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
	},
	
	PAG_SEGURO {
		public int status(final String status) {
			return 0;
		}
		
		public String statusDescription(final int status) {
			return null;
		}
		
		public boolean isPaid(final int status) {
			return false;
		}
		
		public boolean isReadyForAccounting(final int status) {
			return false;
		}
		
		public boolean isCancelled(final int status) {
			return false;
		}
	};
	
	public abstract boolean isPaid(int status);
	public abstract boolean isReadyForAccounting(int status);
	public abstract boolean isCancelled(int status);
	public abstract int status(String status);
	public abstract String statusDescription(int status);
}
