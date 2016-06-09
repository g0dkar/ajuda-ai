package ajuda.ai.model.institution;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import ajuda.ai.model.Slug;
import ajuda.ai.model.extra.CreationInfo;

/**
 * Um post feito por uma {@link Institution Instituição} na página da mesma.
 * 
 * @author Rafael Lins - g0dkar
 *
 */
@Entity
public class InstitutionPost extends Slug {
	/** Serial Version UID */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Institution institution;
	
	@Embedded
	private CreationInfo creationInfo;
	
	@NotBlank
	@Size(max = 0xFF)
	@Column(nullable = false, length = 0xFF)
	private String title;
	
	@NotBlank
	@Column(nullable = false, columnDefinition = "MEDIUMTEXT")
	private String content;
	
	@Column(nullable = false)
	private boolean published;
	
	/**
	 * Make sure this post slug has the {@link #institution} prefix
	 */
	@PrePersist
	@PreUpdate
	public void beforeSave() {
		final String slugPrefix = institution.getSlug() + "/";
		if (!getSlug().startsWith(slugPrefix)) {
			setSlug(slugPrefix + getSlug());
		}
	}
	
	public Institution getInstitution() {
		return institution;
	}

	public void setInstitution(final Institution institution) {
		this.institution = institution;
	}

	public CreationInfo getCreationInfo() {
		return creationInfo;
	}

	public void setCreationInfo(final CreationInfo creationInfo) {
		this.creationInfo = creationInfo;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(final String content) {
		this.content = content;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(final boolean published) {
		this.published = published;
	}
}
