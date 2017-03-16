package ajuda.ai.persistence.model.institution;

import java.util.List;

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
	public InstitutionPersistence(final PersistenceService ps) {
		this.ps = ps;
	}

	@Override
	public Institution get(final Long id) {
		return ps.find(Institution.class, id);
	}
	
	public Institution getSlug(final String slug) {
		return (Institution) query("FROM Institution WHERE slug = :slug").setParameter("slug", slug).getSingleResult();
	}
	
	@Override
	public void persist(final Institution object) {
		ps.persist(object);
	}

	@Override
	public Institution merge(final Institution object) {
		return ps.merge(object);
	}

	@Override
	public Institution remove(final Institution object) {
		ps.remove(object);
		return object;
	}

	@Override
	public Query query(final String query) {
		return ps.createQuery(query);
	}
	
	/**
	 * @return Um array com 2 {@link Number}: A quantidade de ajudantes e o somatório dos valores doados
	 */
	public Object[] getHelperCountDonationsValue(final Institution institution) {
		return (Object[]) query("SELECT count(*), sum(value) FROM Payment WHERE institution = :institution AND paid = true AND cancelled = false").setParameter("institution", institution).getSingleResult();
	}
	
	/**
	 * Adiciona nos {@link Institution#getAttributes() atributos} de uma {@link Institution} os dados de {@link #getHelperCountDonationsValue(Institution)}
	 * nos atributos {@code helpers} e {@code donations}.
	 */
	public void addHelperCountDonationsValue(final Institution institution) {
		final Object[] data = getHelperCountDonationsValue(institution);
		institution.getAttributes().put("helpers", data[0].toString());
		institution.getAttributes().put("donations", data[1] == null ? "0" : data[1].toString());
	}
	
	/**
	 * Adiciona nos {@link Institution#getAttributes() atributos} das {@link Institution instituições} os dados de {@link #getHelperCountDonationsValue(Institution)} de cada uma
	 * nos atributos {@code helpers} e {@code donations}.
	 */
	public void addHelperCountDonationsValue(final List<Institution> institutions) {
		for (final Institution institution : institutions) {
			addHelperCountDonationsValue(institution);
		}
	}
}
