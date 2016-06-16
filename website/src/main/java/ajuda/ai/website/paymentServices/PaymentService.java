package ajuda.ai.website.paymentServices;

import javax.enterprise.context.RequestScoped;

import ajuda.ai.model.billing.PaymentServiceEnum;

@RequestScoped
public class PaymentService {
	public PaymentProcessor get(final PaymentServiceEnum service) {
		switch (service) {
			case MOIP: return null;
			case PAG_SEGURO: return new PagSeguroPaymentProcessor();
			default: return null;
		}
	}
}
