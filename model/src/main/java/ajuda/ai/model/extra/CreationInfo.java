package ajuda.ai.model.extra;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import ajuda.ai.model.activity.Activity;
import ajuda.ai.model.user.KeycloakDBUser;

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

	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date time;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdate;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private KeycloakDBUser creator;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private KeycloakDBUser lastUpdateBy;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Activity creationActivity;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Activity lastUpdateActivity;
	
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

	public KeycloakDBUser getCreator() {
		return creator;
	}

	public void setCreator(final KeycloakDBUser creator) {
		this.creator = creator;
	}

	public KeycloakDBUser getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(final KeycloakDBUser lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

	public Activity getCreationActivity() {
		return creationActivity;
	}

	public void setCreationActivity(final Activity creationActivity) {
		this.creationActivity = creationActivity;
	}

	public Activity getLastUpdateActivity() {
		return lastUpdateActivity;
	}

	public void setLastUpdateActivity(final Activity lastUpdateActivity) {
		this.lastUpdateActivity = lastUpdateActivity;
	}
}
