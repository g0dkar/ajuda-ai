package ajuda.ai.model.institution;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import ajuda.ai.model.Slug;
import ajuda.ai.model.extra.CreationInfo;
import ajuda.ai.model.user.KeycloakDBUser;

/**
 * Representa uma Instituição que será ajudada por seus {@link InstitutionHelper Ajudantes}
 * 
 * @author Rafael Lins - g0dkar
 *
 */
@Entity
public class Institution extends Slug {
	/** Serial Version UID */
	private static final long serialVersionUID = 1L;
	
	@Embedded
	private CreationInfo creation;
	
	@NotBlank
	@Size(max = 255)
	@Column(nullable = false, length = 255)
	private String name;
	
	@NotBlank
	@Size(max = 65535)
	@Column(nullable = false, columnDefinition = "MEDIUMTEXT")
	private String description;
	
	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private InstitutionPost pinnedPost;
	
	@OneToMany(mappedBy = "institution", orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<InstitutionHelper> helpers;
	
	@ManyToMany(fetch = FetchType.LAZY)
	private Set<KeycloakDBUser> leaders;

	/**
	 * @return the creation
	 */
	public CreationInfo getCreation() {
		return creation;
	}

	/**
	 * @param creation the creation to set
	 */
	public void setCreation(final CreationInfo creation) {
		this.creation = creation;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return the pinnedPost
	 */
	public InstitutionPost getPinnedPost() {
		return pinnedPost;
	}

	/**
	 * @param pinnedPost the pinnedPost to set
	 */
	public void setPinnedPost(final InstitutionPost pinnedPost) {
		this.pinnedPost = pinnedPost;
	}

	/**
	 * @return the helpers
	 */
	public Set<InstitutionHelper> getHelpers() {
		return helpers;
	}

	/**
	 * @param helpers the helpers to set
	 */
	public void setHelpers(final Set<InstitutionHelper> helpers) {
		this.helpers = helpers;
	}

	/**
	 * @return the leaders
	 */
	public Set<KeycloakDBUser> getLeaders() {
		return leaders;
	}

	/**
	 * @param leaders the leaders to set
	 */
	public void setLeaders(final Set<KeycloakDBUser> leaders) {
		this.leaders = leaders;
	}
}