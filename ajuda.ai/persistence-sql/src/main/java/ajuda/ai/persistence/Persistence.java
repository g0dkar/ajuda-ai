package ajuda.ai.persistence;

import javax.persistence.Query;

public interface Persistence<T> {
	T get(Long id);
	void persist(T object);
	T merge(T object);
	T remove(T object);
	
	Query query(String query);
}
