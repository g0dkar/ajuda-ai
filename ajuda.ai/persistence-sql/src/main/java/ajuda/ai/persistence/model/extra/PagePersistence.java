package ajuda.ai.persistence.model.extra;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;

import ajuda.ai.model.extra.Page;
import ajuda.ai.persistence.Persistence;
import ajuda.ai.persistence.PersistenceService;

@RequestScoped
public class PagePersistence implements Persistence<Page> {
	private final PersistenceService ps;
	
	/** @deprecated CDI */ @Deprecated
	PagePersistence() { this(null); }
	
	@Inject
	public PagePersistence(PersistenceService ps) {
		this.ps = ps;
	}

	@Override
	public Page get(Long id) {
		return ps.find(Page.class, id);
	}

	@Override
	public void persist(Page object) {
		ps.persist(object);
	}

	@Override
	public Page merge(Page object) {
		return ps.merge(object);
	}

	@Override
	public Page remove(Page object) {
		ps.remove(object);
		return object;
	}

	@Override
	public Query query(String query) {
		return ps.createQuery(query);
	}

}
