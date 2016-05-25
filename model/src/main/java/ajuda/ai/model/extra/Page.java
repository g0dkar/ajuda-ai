package ajuda.ai.model.extra;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import ajuda.ai.util.StringUtils;
import ajuda.ai.util.keycloak.KeycloakUser;

/**
 * A simple page on the Responde.Ai Website. {@link #content} is a Markdown field.
 * 
 * @author Rafael g0dkar
 *	
 */
@Entity
public class Page implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @NotBlank
	@Column(unique = true, length = 0xFF)
	private String id;
	
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
		
		if (id == null) {
			id = StringUtils.asSeoName(title);
		}
	}
	
	public void setCreator(final KeycloakUser user) {
		if (creation == null) {
			creation = new CreationInfo();
		}
		
		creation.setCreator(user.getId());
	}
	
	public void setUpdatedBy(final KeycloakUser user) {
		if (creation != null) {
			creation.setLastUpdateBy(user.getId());
		}
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(final String id) {
		this.id = id;
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
