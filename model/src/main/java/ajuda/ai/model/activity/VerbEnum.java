package ajuda.ai.model.activity;

/**
 * Enum para organizar alguns verbos mais usados. Como ela é armazenada como String há a
 * possibilidade de facilmente adicionar novos verbos.
 * 
 * @author Rafael Lins - g0dkar
 *
 */
public enum VerbEnum {
	CREATED_INSTITUTION("Institution", null),
	DELETED_INSTITUTION("Institution", null),
	UPDATED_INSTITUTION("Institution", null),
	JOINED_INSTITUTION("Institution", null),
	LEFT_INSTITUTION("Institution", null),
	
	HELPED_INSTITUTION("Institution", null),
	HELPED_INSTITUTION_ANON("Institution", null);
	
	public final String entity;
	public final String parentEntity;
	
	private VerbEnum(final String entity, final String parentEntity) {
		this.entity = entity;
		this.parentEntity = parentEntity;
	}
}
