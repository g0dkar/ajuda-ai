package ajuda.ai.model.institution;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ajuda.ai.model.Slug;
import ajuda.ai.model.user.KeycloakDBUser;

/**
 * Representa uma pessoa que está ajudando uma {@link Institution Instituição}
 * 
 * @author Rafael Lins - g0dkar
 *
 */
@Entity
public class InstitutionHelper extends Slug {
	/** Serial Version UID */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Institution institution;
	
	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private KeycloakDBUser creator;
	
	@Min(1)
	@Max(10000000)
	@Column(nullable = false)
	private int value;
	
	@Size(max = 255)
	private String firstName;
	
	@Size(max = 255)
	private String lastName;
	
	@PreUpdate
	@PrePersist
	public void saveUpdate() {
//		setDirectory(institution.getSlug());
		setSlug("helper-" + getId());
	}

	public Institution getInstitution() {
		return institution;
	}

	public void setInstitution(final Institution institution) {
		this.institution = institution;
	}

	public KeycloakDBUser getCreator() {
		return creator;
	}

	public void setCreator(final KeycloakDBUser creator) {
		this.creator = creator;
	}

	public int getValue() {
		return value;
	}

	public void setValue(final int value) {
		this.value = value;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}
}
