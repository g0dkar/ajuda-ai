package ajuda.ai.model.activity;

public enum VerbEnum {
	QUIZ_CREATED("Quiz", "'quiz' as type, entity.id as id, slug as slug, name as name, options.theme as theme, options.fontText as fontText, options.fontTitle as fontTitle", null, null),
	QUIZ_UPDATED("Quiz", "'quiz' as type, entity.id as id, slug as slug, name as name, options.theme as theme, options.fontText as fontText, options.fontTitle as fontTitle", null, null),
	QUIZ_QUESTIONS_UPDATE("Quiz", "'quiz' as type, entity.id as id, slug as slug, name as name, options.theme as theme, options.fontText as fontText, options.fontTitle as fontTitle", null, null),
	QUIZ_QUESTIONS_REMOVE("Quiz", "'quiz' as type, entity.id as id, slug as slug, name as name, options.theme as theme, options.fontText as fontText, options.fontTitle as fontTitle", null, null),
	
//	PLAYER_CREATED(null, null, null, null),
//	PLAYER_JOINED_GAME(null, null, null, null),
//	PLAYER_LEFT_GAME(null, null, null, null),
//	PLAYER_FINISHED_GAME(null, null, null, null),
//	PLAYER_ANSWERED_QUESTION(null, null, null, null),
//	PLAYER_CHANGED_ANSWER(null, null, null, null),
//
//	GAMESESSION_OPENED(null, null, null, null),
//	GAMESESSION_STARTED(null, null, null, null),
//	GAMESESSION_CHANGED_QUESTION(null, null, null, null),
//	GAMESESSION_FINISHED(null, null, null, null),
//	GAMESESSION_RESTARTED(null, null, null, null),
//	GAMESESSION_CLOSED(null, null, null, null),
	
	GROUP_CREATED("Group", "'group' as type, entity.id as id, slug as slug, name as name, options.theme as theme, options.fontText as fontText, options.fontTitle as fontTitle", null, null),
	GROUP_UPDATED("Group", "'group' as type, entity.id as id, slug as slug, name as name, options.theme as theme, options.fontText as fontText, options.fontTitle as fontTitle", null, null),
	GROUP_JOIN("Group", "'group' as type, entity.id as id, slug as slug, name as name, options.theme as theme, options.fontText as fontText, options.fontTitle as fontTitle", null, null),
	GROUP_LEAVE("Group", "'group' as type, entity.id as id, slug as slug, name as name, options.theme as theme, options.fontText as fontText, options.fontTitle as fontTitle", null, null),
	GROUP_ROLE_ADD("Group", "'group' as type, entity.id as id, slug as slug, name as name, options.theme as theme, options.fontText as fontText, options.fontTitle as fontTitle", null, null),
	GROUP_ROLE_REMOVE("Group", "'group' as type, entity.id as id, slug as slug, name as name, options.theme as theme, options.fontText as fontText, options.fontTitle as fontTitle", null, null),
	GROUP_ATJ("Group", "'group' as type, entity.id as id, slug as slug, name as name, options.theme as theme, options.fontText as fontText, options.fontTitle as fontTitle", null, null),
	GROUP_ATJ_ACCEPT("Group", "'group' as type, entity.id as id, slug as slug, name as name, options.theme as theme, options.fontText as fontText, options.fontTitle as fontTitle", null, null),
	GROUP_ATJ_REJECT("Group", "'group' as type, entity.id as id, slug as slug, name as name, options.theme as theme, options.fontText as fontText, options.fontTitle as fontTitle", null, null),
	GROUP_BLOCK("Group", "'group' as type, entity.id as id, slug as slug, name as name, options.theme as theme, options.fontText as fontText, options.fontTitle as fontTitle", null, null),
	GROUP_UNBLOCK("Group", "'group' as type, entity.id as id, slug as slug, name as name, options.theme as theme, options.fontText as fontText, options.fontTitle as fontTitle", null, null);
	
	public final String entityName;
	public final String entityFields;
	public final String parentEntityName;
	public final String parentEntityFields;
	
	private VerbEnum(final String entityName, final String entityFields, final String parentEntityName, final String parentEntityFields) {
		this.entityName = entityName;
		this.entityFields = entityFields;
		this.parentEntityName = parentEntityName;
		this.parentEntityFields = parentEntityFields;
	}
}
