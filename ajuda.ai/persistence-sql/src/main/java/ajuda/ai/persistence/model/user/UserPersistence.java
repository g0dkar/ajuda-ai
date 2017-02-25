package ajuda.ai.persistence.model.user;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.Query;

import ajuda.ai.model.user.User;
import ajuda.ai.persistence.Persistence;
import ajuda.ai.persistence.PersistenceService;

@RequestScoped
public class UserPersistence implements Persistence<User> {
	private final PersistenceService ps;
	
	/** @deprecated CDI */ @Deprecated
	UserPersistence() { this(null); }
	
	@Inject
	public UserPersistence(PersistenceService ps) {
		this.ps = ps;
	}

	@Override
	public User get(Long id) {
		return ps.find(User.class, id);
	}
	
	@Override
	public void persist(User object) {
		ps.persist(object);
	}

	@Override
	public User merge(User object) {
		return ps.merge(object);
	}

	@Override
	public User remove(User object) {
		ps.remove(object);
		return object;
	}

	@Override
	public Query query(String query) {
		return ps.createQuery(query);
	}

}
