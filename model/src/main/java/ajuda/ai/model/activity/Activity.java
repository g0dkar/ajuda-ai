package ajuda.ai.model.activity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Pattern.Flag;

@Entity
@Table(indexes = {
	@Index(columnList = "timestamp", name = "timestamp"),
	@Index(columnList = "entity, verb", name = "entity___verb"),
	@Index(columnList = "actor, entity, parentEntity", name = "actor___entity___parentEntity")
})
public class Activity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	
	@Column(nullable = false, length = 32)
	@Enumerated(EnumType.STRING)
	private VerbEnum verb;
	
	@NotNull
	@Column(nullable = false, length = 36)
	@Pattern(regexp = "[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}", flags = { Flag.CASE_INSENSITIVE })
	private String actor;
	
	private Long entity;
	private Long parentEntity;
	
	@Column(length = 1024)
	private String extra;
	
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

	public String getActor() {
		return actor;
	}

	public void setActor(final String actor) {
		this.actor = actor;
	}

	public Long getEntity() {
		return entity;
	}

	public void setEntity(final Long entity) {
		this.entity = entity;
	}

	public Long getParentEntity() {
		return parentEntity;
	}

	public void setParentEntity(final Long parentEntity) {
		this.parentEntity = parentEntity;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(final String extra) {
		this.extra = extra;
	}
}
