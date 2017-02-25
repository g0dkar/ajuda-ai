package ajuda.ai.persistence.model.institution;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;

import ajuda.ai.model.institution.Institution;
import ajuda.ai.model.institution.InstitutionPost;
import ajuda.ai.persistence.Persistence;
import ajuda.ai.persistence.PersistenceService;

@RequestScoped
public class InstitutionPostPersistence implements Persistence<InstitutionPost> {
	private final PersistenceService ps;
	
	/** @deprecated CDI */ @Deprecated
	InstitutionPostPersistence() { this(null); }
	
	@Inject
	public InstitutionPostPersistence(PersistenceService ps) {
		this.ps = ps;
	}
	
	@Override
	public InstitutionPost get(Long id) {
		return ps.find(InstitutionPost.class, id);
	}
	
	public InstitutionPost getSlug(String slug, Institution institution) {
		return (InstitutionPost) query("FROM InstitutionPost WHERE institution = :institution AND slug = :slug").setParameter("institution", institution).setParameter("slug", slug).getSingleResult();
	}
	
	@Override
	public void persist(InstitutionPost object) {
		ps.persist(object);
	}
	
	@Override
	public InstitutionPost merge(InstitutionPost object) {
		return ps.merge(object);
	}
	
	@Override
	public InstitutionPost remove(InstitutionPost object) {
		ps.remove(object);
		return object;
	}
	
	@Override
	public Query query(String query) {
		return ps.createQuery(query);
	}

}
