package ajuda.ai.persistence.model;

import java.time.Instant;
import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;

import ajuda.ai.model.Configuration;
import ajuda.ai.persistence.Persistence;
import ajuda.ai.persistence.PersistenceService;
import ajuda.ai.util.StringUtils;

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
	
	public Configuration getConfiguration(String name) {
		return (Configuration) ps.createQuery("FROM Configuration WHERE name = :name").setParameter("name", name).getSingleResult();
	}
	
	public String get(String configuration) {
		return get(configuration, (String) null);
	}
	
	public String get(String configuration, String defaultValue) {
		String value = (String) ps.createQuery("SELECT value FROM Configuration WHERE name = :name").setParameter("name", configuration).getSingleResult();
		return value != null ? value : defaultValue;
	}
	
	public void set(String configuration, String value) {
		Configuration conf = getConfiguration(configuration);
		if (conf == null) {
			conf = new Configuration();
			conf.setName(configuration);
		}
		conf.setValue(value);
		
		if (conf.getId() == null) {
			persist(conf);
		}
		else {
			merge(conf);
		}
	}
	
	public int get(String configuration, int defaultValue) {
		String value = get(configuration);
		return StringUtils.isNumber(value) ? Integer.parseInt(value, 10) : defaultValue;
	}
	
	public void set(String configuration, int value) {
		set(configuration, Integer.toString(value, 10));
	}
	
	public long get(String configuration, long defaultValue) {
		String value = get(configuration);
		return StringUtils.isNumber(value) ? Long.parseLong(value, 10) : defaultValue;
	}
	
	public void set(String configuration, long value) {
		set(configuration, Long.toString(value, 10));
	}
	
	public double get(String configuration, double defaultValue) {
		String value = get(configuration);
		
		try {
			return Double.parseDouble(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public void set(String configuration, double value) {
		set(configuration, Double.toString(value));
	}
	
	public Instant get(String configuration, Instant defaultValue) {
		String value = get(configuration);
		
		try {
			return Instant.parse(value);
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public void set(String configuration, Instant value) {
		set(configuration, value.toString());
	}
	
	public Date get(String configuration, Date defaultValue) {
		String value = get(configuration);
		
		try {
			return new Date(Instant.parse(value).toEpochMilli());
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public void set(String configuration, Date value) {
		set(configuration, Instant.ofEpochMilli(value.getTime()).toString());
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
