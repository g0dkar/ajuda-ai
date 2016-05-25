package ajuda.ai.model.extra;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import ajuda.ai.model.Slug;
import ajuda.ai.util.StringUtils;

/**
 * Uma página de conteúdo normal em markdown.
 * 
 * @author Rafael Lins - g0dkar
 *	
 */
@Entity
public class Page extends Slug implements Serializable {
	private static final long serialVersionUID = 1L;

	@Embedded
	private CreationInfo creation;
	
	@NotBlank
	@Size(max = 0xFF)
	@Column(nullable = false, length = 0xFF)
	private String title;
	
	@NotBlank
	@Column(nullable = false, columnDefinition = "MEDIUMTEXT")
	private String content;
	
	@Column(nullable = false)
	private boolean published;
	
	@PreUpdate
	@PrePersist
	public void beforeSave() {
		if (title != null) {
			title = title.trim().replaceAll("\\s+", " ");
		}
		
		if (content != null) {
			content = content.trim();
		}
		
		if (getSlug() == null) {
			setSlug(StringUtils.slug(title));
		}
		
		setDirectory(null);
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
