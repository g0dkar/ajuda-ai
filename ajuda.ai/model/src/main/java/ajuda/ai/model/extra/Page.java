package ajuda.ai.model.extra;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import com.google.gson.annotations.Expose;

import ajuda.ai.util.StringUtils;

/**
 * Uma página de conteúdo normal em markdown.
 * 
 * @author Rafael Lins - g0dkar
 *	
 */
@Entity
public class Page implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Expose
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** "Endereço" desta entidade. Se {@code exemplo} for o slug podemos ter algo como {@code https://ajuda.ai/exemplo} */
	@Expose
	@NotBlank
	@Size(min = 2, max = 64)
	@Column(nullable = false, unique = true, length = 64)
	@Pattern(regexp = "[a-z][a-z0-9\\-]*[a-z0-9](/[a-z][a-z0-9\\-]*[a-z0-9])?")
	private String slug;

	@Embedded
	private CreationInfo creation;
	
	@URL
	@Size(max = 1024)
	@Column(length = 1024)
	private String headerImage;
	
	@NotBlank
	@Size(max = 255)
	@Column(nullable = false, length = 255)
	private String headerLine1;
	
	@Size(max = 255)
	@Column(length = 255)
	private String headerLine2;
	
	@NotBlank
	@Size(max = 255)
	@Column(nullable = false, length = 255)
	private String title;
	
	@Size(max = 255)
	@Column(length = 255)
	private String subtitle;
	
	@NotBlank
	@Column(nullable = false, columnDefinition = "MEDIUMTEXT")
	private String content;
	
	@Column(nullable = false)
	private boolean published;
	
	@PreUpdate
	@PrePersist
	public void beforeSave() {
		if (title != null) {
			title = title.replaceAll("\\s+", " ").trim();
		}
		
		if (content != null) {
			content = content.trim();
		}
		
		if (slug == null) {
			slug = StringUtils.slug(title);
		}
	}
	
	public CreationInfo getCreation() {
		return creation;
	}
	
	public void setCreation(final CreationInfo creation) {
		this.creation = creation;
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
	
	public String getContentMarkdown() {
		return StringUtils.markdown(content);
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

	public String getHeaderImage() {
		return headerImage;
	}

	public void setHeaderImage(final String headerImage) {
		this.headerImage = headerImage;
	}

	public String getHeaderLine1() {
		return headerLine1;
	}

	public void setHeaderLine1(final String headerLine1) {
		this.headerLine1 = headerLine1;
	}

	public String getHeaderLine2() {
		return headerLine2;
	}

	public void setHeaderLine2(final String headerLine2) {
		this.headerLine2 = headerLine2;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(final String subtitle) {
		this.subtitle = subtitle;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}
}
