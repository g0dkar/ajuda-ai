package ajuda.ai.persistence.model;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;

import ajuda.ai.model.Configuration;
import ajuda.ai.persistence.Persistence;
import ajuda.ai.persistence.PersistenceService;

/**
 * DAO for {@link Configuration}
 * @author Rafael Lins
 *
 */
@RequestScoped
public class ConfigurationPersistence implements Persistence<Configuration> {
	private final PersistenceService ps;
	
	/** @deprecated CDI */ @Deprecated
	ConfigurationPersistence() { this(null); }
	
	@Inject
	public ConfigurationPersistence(PersistenceService ps) {
		this.ps = ps;
	}

	@Override
	public Configuration get(Long id) {
		return ps.find(Configuration.class, id);
	}

	@Override
	public void persist(Configuration object) {
		ps.persist(object);
	}

	@Override
	public Configuration merge(Configuration object) {
		return ps.merge(object);
	}

	@Override
	public Configuration remove(Configuration object) {
		ps.remove(object);
		return object;
	}

	@Override
	public Query query(String query) {
		return ps.createQuery(query);
	}
}
