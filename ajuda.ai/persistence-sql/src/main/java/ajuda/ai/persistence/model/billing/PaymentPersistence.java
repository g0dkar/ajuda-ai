package ajuda.ai.persistence.model.billing;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;

import ajuda.ai.model.billing.Payment;
import ajuda.ai.persistence.Persistence;
import ajuda.ai.persistence.PersistenceService;

@RequestScoped
public class PaymentPersistence implements Persistence<Payment> {
	private final PersistenceService ps;
	
	/** @deprecated CDI */ @Deprecated
	PaymentPersistence() { this(null); }
	
	@Inject
	public PaymentPersistence(PersistenceService ps) {
		this.ps = ps;
	}

	@Override
	public Payment get(Long id) {
		return ps.find(Payment.class, id);
	}

	@Override
	public void persist(Payment object) {
		ps.persist(object);
	}

	@Override
	public Payment merge(Payment object) {
		return ps.merge(object);
	}

	@Override
	public Payment remove(Payment object) {
		ps.remove(object);
		return object;
	}

	@Override
	public Query query(String query) {
		return ps.createQuery(query);
	}

}
