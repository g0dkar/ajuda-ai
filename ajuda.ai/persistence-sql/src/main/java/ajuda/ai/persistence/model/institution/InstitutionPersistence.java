package ajuda.ai.persistence.model.institution;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;

import ajuda.ai.model.institution.Institution;
import ajuda.ai.persistence.Persistence;
import ajuda.ai.persistence.PersistenceService;

@RequestScoped
public class InstitutionPersistence implements Persistence<Institution> {
	private final PersistenceService ps;
	
	/** @deprecated CDI */ @Deprecated
	InstitutionPersistence() { this(null); }
	
	@Inject
	public InstitutionPersistence(PersistenceService ps) {
		this.ps = ps;
	}

	@Override
	public Institution get(Long id) {
		return ps.find(Institution.class, id);
	}
	
	public Institution getSlug(String slug) {
		return (Institution) query("FROM Institution WHERE slug = :slug").setParameter("slug", slug).getSingleResult();
	}
	
	@Override
	public void persist(Institution object) {
		ps.persist(object);
	}

	@Override
	public Institution merge(Institution object) {
		return ps.merge(object);
	}

	@Override
	public Institution remove(Institution object) {
		ps.remove(object);
		return object;
	}

	@Override
	public Query query(String query) {
		return ps.createQuery(query);
	}

}
