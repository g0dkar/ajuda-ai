package ajuda.ai.persistence.model.user;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;

import org.mindrot.jbcrypt.BCrypt;

import ajuda.ai.model.user.User;
import ajuda.ai.persistence.Persistence;
import ajuda.ai.persistence.PersistenceService;
import ajuda.ai.persistence.model.ConfigurationPersistence;

@RequestScoped
public class UserPersistence implements Persistence<User> {
	private final PersistenceService ps;
	private final ConfigurationPersistence conf;
	
	/** @deprecated CDI */ @Deprecated
	UserPersistence() { this(null, null); }
	
	@Inject
	public UserPersistence(final PersistenceService ps, final ConfigurationPersistence conf) {
		this.ps = ps;
		this.conf = conf;
	}

	@Override
	public User get(final Long id) {
		return ps.find(User.class, id);
	}
	
	public User getUsername(final String username) {
		return (User) ps.createQuery("FROM User WHERE username = :username").setParameter("username", username).getSingleResult();
	}
	
	public User getUsernameOrEmail(final String username) {
		return (User) ps.createQuery("FROM User WHERE username = :username OR email = :username").setParameter("username", username).getSingleResult();
	}
	
	@Override
	public void persist(final User object) {
		if (!object.getPassword().matches("\\$[0-9a-zA-Z]{2}\\$\\d{1,2}\\$.+")) {
			object.setPassword(BCrypt.hashpw(object.getPassword(), BCrypt.gensalt(conf.get("bcrypt.log_rounds", 10))));
		}
		
		ps.persist(object);
	}
	
	@Override
	public User merge(final User object) {
		if (object.getPassword() == null) {
			object.setPassword((String) ps.createQuery("SELECT password FROM User WHERE id = :id").setParameter("id", object.getId()).getSingleResult());
		}
		else if (!object.getPassword().matches("\\$[0-9a-zA-Z]{2}\\$\\d{1,2}\\$.+")) {
			object.setPassword(BCrypt.hashpw(object.getPassword(), BCrypt.gensalt(conf.get("bcrypt.log_rounds", 10))));
		}
		
		return ps.merge(object);
	}
	
	public void setPassword(final User user, final String password) {
		user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt(conf.get("bcrypt.log_rounds", 10))));
	}

	@Override
	public User remove(final User object) {
		ps.remove(object);
		return object;
	}

	@Override
	public Query query(final String query) {
		return ps.createQuery(query);
	}

}
