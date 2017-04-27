package ajuda.ai.persistence.model.billing;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;

import ajuda.ai.model.billing.Payment;
import ajuda.ai.model.billing.PaymentEvent;
import ajuda.ai.persistence.Persistence;
import ajuda.ai.persistence.PersistenceService;

@RequestScoped
public class PaymentPersistence implements Persistence<Payment> {
	private final PersistenceService ps;
	
	/** @deprecated CDI */ @Deprecated
	PaymentPersistence() { this(null); }
	
	@Inject
	public PaymentPersistence(final PersistenceService ps) {
		this.ps = ps;
	}

	@Override
	public Payment get(final Long id) {
		return ps.find(Payment.class, id);
	}
	
	public Payment get(final String uuid) {
		return (Payment) ps.createQuery("FROM Payment WHERE uuid = :uuid").setParameter("uuid", uuid).getSingleResult();
	}

	@Override
	public void persist(final Payment object) {
		ps.persist(object);
	}
	
	public void persist(final PaymentEvent object) {
		ps.persist(object);
	}

	@Override
	public Payment merge(final Payment object) {
		return ps.merge(object);
	}

	@Override
	public Payment remove(final Payment object) {
		ps.remove(object);
		return object;
	}

	@Override
	public Query query(final String query) {
		return ps.createQuery(query);
	}

}
