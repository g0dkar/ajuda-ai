package ajuda.ai.model.institution;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class HelperLoginRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @Column(length = 32)
	private String id;
	
	@ManyToOne(optional = false)
	private Helper helper;
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	
	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date useTimestamp;
	
	@Column(nullable = false)
	private boolean active;
	
	@PrePersist
	public void beforeSave() {
		if (id == null) { id = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase(); }
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public Helper getHelper() {
		return helper;
	}

	public void setHelper(final Helper helper) {
		this.helper = helper;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(final Date timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(final boolean active) {
		this.active = active;
	}

	public Date getUseTimestamp() {
		return useTimestamp;
	}

	public void setUseTimestamp(final Date useTimestamp) {
		this.useTimestamp = useTimestamp;
	}
}
