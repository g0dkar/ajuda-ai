package ajuda.ai.model.extra;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Pattern.Flag;

import ajuda.ai.model.activity.Activity;

/**
 * Info about the creation and update of an Entity
 * 
 * @author g0dkar
 *
 */
@Embeddable
public class CreationInfo {
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date time;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdate;
	
	@NotNull
	@Column(nullable = false, length = 36)
	@Pattern(regexp = "[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}", flags = { Flag.CASE_INSENSITIVE })
	private String creator;
	
	@Column(length = 36)
	@Pattern(regexp = "[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}", flags = { Flag.CASE_INSENSITIVE })
	private String lastUpdateBy;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Activity creationActivity;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Activity lastUpdateActivity;
	
	@PrePersist
	public void beforeSave() {
		if (time == null) {
			time = new Date();
		}
	}
	
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

	public String getCreator() {
		return creator;
	}

	public void setCreator(final String creator) {
		this.creator = creator;
	}

	public String getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(final String lastUpdateBy) {
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
