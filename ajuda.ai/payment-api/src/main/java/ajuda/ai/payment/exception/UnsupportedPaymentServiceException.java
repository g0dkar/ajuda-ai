package ajuda.ai.payment.exception;

/**
 * Exceção que indica que o serviço de pagamento requisitado não é suportado
 * @author Rafael Lins
 *
 */
public class UnsupportedPaymentServiceException extends RuntimeException {
	private static final long serialVersionUID = 5985711746057579224L;

	public UnsupportedPaymentServiceException() {
		super();
	}

	public UnsupportedPaymentServiceException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public UnsupportedPaymentServiceException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public UnsupportedPaymentServiceException(String arg0) {
		super(arg0);
	}

	public UnsupportedPaymentServiceException(Throwable arg0) {
		super(arg0);
	}
}
