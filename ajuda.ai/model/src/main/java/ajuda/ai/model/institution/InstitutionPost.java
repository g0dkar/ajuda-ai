package ajuda.ai.model.institution;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import com.google.gson.annotations.Expose;

import ajuda.ai.model.extra.CreationInfo;

/**
 * Um post feito por uma {@link Institution Instituição} na página da mesma.
 * 
 * @author Rafael Lins - g0dkar
 *
 */
@Entity
public class InstitutionPost implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 1L;
	
	@Expose
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Expose
	@NotBlank
	@Size(min = 2, max = 128)
	@Column(nullable = false, unique = true, length = 128)
	@Pattern(regexp = "[a-z][a-z0-9\\-]*[a-z0-9](/[a-z][a-z0-9\\-]*[a-z0-9])?")
	private String slug;
	
	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Institution institution;
	
	@Expose
	@Embedded
	private CreationInfo creation;
	
	@URL
	@Size(max = 1024)
	@Column(length = 1024)
	private String headerImage;
	
	@NotBlank
	@Size(max = 128)
	@Column(nullable = false, length = 128)
	private String title;
	
	@Size(max = 128)
	@Column(length = 128)
	private String subtitle;
	
	@NotBlank
	@Size(max = 65525)
	@Column(nullable = false, columnDefinition = "MEDIUMTEXT")
	private String content;
	
	@Column(nullable = false)
	private boolean published;
	
	@Column(nullable = false)
	private long pageviews;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(final String slug) {
		this.slug = slug;
	}

	public Institution getInstitution() {
		return institution;
	}

	public void setInstitution(final Institution institution) {
		this.institution = institution;
	}

	public CreationInfo getCreation() {
		return creation;
	}

	public void setCreation(final CreationInfo creation) {
		this.creation = creation;
	}

	public String getHeaderImage() {
		return headerImage;
	}

	public void setHeaderImage(final String headerImage) {
		this.headerImage = headerImage;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(final String subtitle) {
		this.subtitle = subtitle;
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

	public long getPageviews() {
		return pageviews;
	}

	public void setPageviews(final long pageviews) {
		this.pageviews = pageviews;
	}
}
