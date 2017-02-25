package ajuda.ai.backend.util;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.bean.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Parameter;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

/**
 * Handles a few stuff about an {@link EntityManager} (which is injected)
 * 
 * @author g0dkar
 *
 */
@RequestScoped
public class PersistenceService {
	@PersistenceContext
	private EntityManager entityManager;
	
	public EntityManager em() {
		return entityManager;
	}
	
	public void persist(final Object entity) {
		entityManager.persist(entity);
	}

	public <T> T merge(final T entity) {
		return entityManager.merge(entity);
	}

	public void remove(final Object entity) {
		entityManager.remove(entity);
	}

	public <T> T find(final Class<T> entityClass, final Object primaryKey) {
		return entityManager.find(entityClass, primaryKey);
	}

	public <T> T find(final Class<T> entityClass, final Object primaryKey, final LockModeType lockMode) {
		return entityManager.find(entityClass, primaryKey, lockMode);
	}

	public void flush() {
		entityManager.flush();
	}

	public void lock(final Object entity, final LockModeType lockMode) {
		entityManager.lock(entity, lockMode);
	}

	public void refresh(final Object entity) {
		entityManager.refresh(entity);
	}

	public void refresh(final Object entity, final LockModeType lockMode) {
		entityManager.refresh(entity, lockMode);
	}

	public void detach(final Object entity) {
		entityManager.detach(entity);
	}

	public boolean contains(final Object entity) {
		return entityManager.contains(entity);
	}

	public Query createQuery(final String qlString) {
		return new NiceQuery(entityManager.createQuery(qlString));
	}
	
	public Query createNativeQuery(final String qlString) {
		return new NiceQuery(entityManager.createNativeQuery(qlString));
	}

	public <T> TypedQuery<T> createQuery(final String qlString, final Class<T> resultClass) {
		return entityManager.createQuery(qlString, resultClass);
	}
	
	/**
	 * Wraps a {@link Query} object and implements some nice features (or just avoid annoying crap)
	 * 
	 * @author g0dkar
	 *
	 */
	public static class NiceQuery implements Query {
		private final Query query;
		
		public NiceQuery(final Query query) {
			this.query = query;
		}
		
		public List getResultList() {
			return query.getResultList();
		}
		
		public Object getSingleResult() {
			final List results = query.getResultList();
			return results != null && !results.isEmpty() ? results.get(0) : null;
//			return query.getSingleResult();
		}

		public int executeUpdate() {
			return query.executeUpdate();
		}

		public Query setMaxResults(final int maxResult) {
			query.setMaxResults(maxResult);
			return this;
		}

		public int getMaxResults() {
			return query.getMaxResults();
		}

		public Query setFirstResult(final int startPosition) {
			query.setFirstResult(startPosition);
			return this;
		}

		public int getFirstResult() {
			return query.getFirstResult();
		}

		public Query setHint(final String hintName, final Object value) {
			query.setHint(hintName, value);
			return this;
		}

		public Map<String, Object> getHints() {
			return query.getHints();
		}

		public <T> Query setParameter(final Parameter<T> param, final T value) {
			query.setParameter(param, value);
			return this;
		}

		public Query setParameter(final Parameter<Calendar> param, final Calendar value, final TemporalType temporalType) {
			query.setParameter(param, value, temporalType);
			return this;
		}

		public Query setParameter(final Parameter<Date> param, final Date value, final TemporalType temporalType) {
			query.setParameter(param, value, temporalType);
			return this;
		}

		public Query setParameter(final String name, final Object value) {
			query.setParameter(name, value);
			return this;
		}

		public Query setParameter(final String name, final Calendar value, final TemporalType temporalType) {
			query.setParameter(name, value, temporalType);
			return this;
		}

		public Query setParameter(final String name, final Date value, final TemporalType temporalType) {
			query.setParameter(name, value, temporalType);
			return this;
		}

		public Query setParameter(final int position, final Object value) {
			query.setParameter(position, value);
			return this;
		}

		public Query setParameter(final int position, final Calendar value, final TemporalType temporalType) {
			query.setParameter(position, value, temporalType);
			return this;
		}

		public Query setParameter(final int position, final Date value, final TemporalType temporalType) {
			query.setParameter(position, value, temporalType);
			return this;
		}

		public Set<Parameter<?>> getParameters() {
			return query.getParameters();
		}

		public Parameter<?> getParameter(final String name) {
			return query.getParameter(name);
		}

		public <T> Parameter<T> getParameter(final String name, final Class<T> type) {
			return query.getParameter(name, type);
		}

		public Parameter<?> getParameter(final int position) {
			return query.getParameter(position);
		}

		public <T> Parameter<T> getParameter(final int position, final Class<T> type) {
			return query.getParameter(position, type);
		}

		public boolean isBound(final Parameter<?> param) {
			return query.isBound(param);
		}

		public <T> T getParameterValue(final Parameter<T> param) {
			return query.getParameterValue(param);
		}

		public Object getParameterValue(final String name) {
			query.getParameterValue(name);
			return this;
		}

		public Object getParameterValue(final int position) {
			query.getParameterValue(position);
			return this;
		}

		public Query setFlushMode(final FlushModeType flushMode) {
			query.setFlushMode(flushMode);
			return this;
		}

		public FlushModeType getFlushMode() {
			return query.getFlushMode();
		}

		public Query setLockMode(final LockModeType lockMode) {
			query.setLockMode(lockMode);
			return this;
		}

		public LockModeType getLockMode() {
			return query.getLockMode();
		}

		public <T> T unwrap(final Class<T> cls) {
			return query.unwrap(cls);
		}
	}
}
