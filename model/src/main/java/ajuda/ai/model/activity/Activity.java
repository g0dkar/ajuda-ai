package ajuda.ai.model.activity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ajuda.ai.model.user.KeycloakDBUser;

/**
 * <p>
 * Uma atividade que algo ou alguém fez dentro do sistema. Utilizado como meio de implementar um
 * stream de atividades que estão acontecendo no sistema.
 * 
 * <p>
 * Ela tenta representar, no banco de dados, a seguinte estrutura frasal: {@code <ator> <verbo>
 * <entidade> [que pertence a <entidade-mae>]}
 * 
 * <p>
 * Por exemplo: "{@code <Rafael> <criou> a instituição <Exemplo>}". Essa frase seria guardada no
 * banco de dados como:
 * 
 * <ul>
 * <li>ator = {@code <id do usuário 'Rafael'>}
 * <li>verbo = {@code criou_instituicao} (note que o responsável por determinar que entidade o id em
 * {@link #entity} representa é o verbo)
 * <li>entidade = {@code <id da instiuição 'Exemplo'>}
 * </ul>
 * 
 * @author Rafael Lins - g0dkar
 *
 */
@Entity
@Table(indexes = { @Index(columnList = "timestamp, actor, verb, entity", name = "all_fields") })
public class Activity implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	
	@Column(nullable = false, length = 36)
	@Enumerated(EnumType.STRING)
	private VerbEnum verb;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private KeycloakDBUser actor;
	
	private Long entity;
	
	@PrePersist
	public void beforeSave() {
		if (timestamp == null) {
			timestamp = new Date();
		}
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(final Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public VerbEnum getVerb() {
		return verb;
	}
	
	public void setVerb(final VerbEnum verb) {
		this.verb = verb;
	}
	
	public KeycloakDBUser getActor() {
		return actor;
	}
	
	public void setActor(final KeycloakDBUser actor) {
		this.actor = actor;
	}
	
	public Long getEntity() {
		return entity;
	}
	
	public void setEntity(final Long entity) {
		this.entity = entity;
	}
}
