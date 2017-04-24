package ajuda.ai.model.extra;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ajuda.ai.model.user.User;

/**
 * Info about the creation and update of an Entity
 * 
 * @author g0dkar
 *
 */
@Embeddable
public class CreationInfo implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 1L;
	
	@Column(nullable = false, name = "creation_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date time;
	
	@Column(name = "last_update_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdate;
	
	@ManyToOne
	private User creator;
	
	@ManyToOne
	private User lastUpdateBy;
	
	/**
	 * Before saving, set the creation time if not already set
	 */
	@PrePersist
	public void beforeSave() {
		if (time == null) {
			time = new Date();
		}
	}
	
	/**
	 * Before updating, set the last update time regardless of what is already set
	 */
	@PreUpdate
	public void beforeUpdate() {
		lastUpdate = new Date();
	}

	public Date getTime() {
		return time;
	}

	public void setTime(final Date time) {
		this.time = time;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(final Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(final User creator) {
		this.creator = creator;
	}

	public User getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(final User lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}
}
