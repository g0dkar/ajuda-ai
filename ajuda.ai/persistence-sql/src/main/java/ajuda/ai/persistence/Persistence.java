package ajuda.ai.persistence;

import javax.persistence.Query;

/**
 * Implementa uma interface para persistência de dados em diferentes sistemas de persistência
 * @author Rafael Lins
 *
 * @param <T> Entidade que será persistida
 */
public interface Persistence<T> {
	/**
	 * @param id Identificador da entidade
	 * @return Retorna uma entidade a partir do ID da mesma
	 */
	T get(Long id);
	
	/**
	 * Persiste uma entidade no sistema de persistência
	 * @param object Entidade para persistir
	 */
	void persist(T object);
	
	/**
	 * Salva alterações em uma entidade já existente no sistema de persistência
	 * @param object Entidade para atualizar
	 * @return Entidade atualizada
	 */
	T merge(T object);
	
	/**
	 * Remove uma entidade do sistema de persistência
	 * @param object Entidade para remover
	 * @return Entidade removida
	 */
	T remove(T object);
	
	/**
	 * Executa uma {@link Query} junto ao sistema de persistência da entidade
	 * @param query
	 * @return {@link Query} para comunicação junto ao sistema de persistência
	 */
	Query query(String query);
}
