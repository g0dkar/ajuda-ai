package ajuda.ai.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Pattern.Flag;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

/**
 * <p>Entidade "mãe" de todas as coisas no website que compartilharão um "slug". Slugs são aquelas
 * partes da URL que servem como ID de algo: {@code https://ajuda.ai/<slug>}
 * 
 * <p>Por exemplo, {@code https://ajuda.ai/exemplo} o slug que estamos buscando será {@code exemplo}
 * 
 * @author Rafael Lins - g0dkar
 *
 */
@Entity
@Table(name = "slugs", indexes = { @Index(columnList = "slug, directory", name = "unique_slug_dir", unique = true) })
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Slug implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/** O "diretório" do slug. Isso permite coisas como {@code https://ajuda.ai/exemplo/slug-dentro-de-exemplo} */
	@Column(length = 255)
	@Size(max = 255)
	@Pattern(regexp = "[a-z][a-z0-9\\-]*[a-z0-9]", flags = { Flag.CASE_INSENSITIVE })
	private String directory;
	
	/** "Endereço" desta entidade. Se {@code exemplo} for o slug podemos ter algo como {@code https://ajuda.ai/exemplo} */
	@NotBlank
	@Size(max = 255)
	@Column(nullable = false, length = 255)
	@Pattern(regexp = "[a-z][a-z0-9\\-]*[a-z0-9]", flags = { Flag.CASE_INSENSITIVE })
	private String slug;
	
	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public String getDirectory() {
		return directory;
	}
	
	public void setDirectory(final String directory) {
		this.directory = directory != null ? directory.toLowerCase() : null;
	}
	
	public String getSlug() {
		return slug;
	}
	
	public void setSlug(final String slug) {
		this.slug = slug != null ? slug.toLowerCase() : null;
	}
}
