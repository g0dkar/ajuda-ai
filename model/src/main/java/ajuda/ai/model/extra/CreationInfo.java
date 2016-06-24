package ajuda.ai.model.extra;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Pattern.Flag;

import com.google.gson.annotations.Expose;

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

	@Expose
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date time;
	
	@Expose
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdate;
	
	@Expose
	@NotNull
	@Column(nullable = false, length = 36)
	@Pattern(regexp = "[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}", flags = { Flag.CASE_INSENSITIVE })
	private String creator;
	
	@Expose
	@Column(length = 36)
	@Pattern(regexp = "[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}", flags = { Flag.CASE_INSENSITIVE })
	private String lastUpdateBy;
	
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
}
