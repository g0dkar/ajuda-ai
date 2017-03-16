package ajuda.ai.model.institution;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import ajuda.ai.model.extra.Page;

/**
 * Um post feito por uma {@link Institution Instituição} na página da mesma.
 * 
 * @author Rafael Lins - g0dkar
 *
 */
@Entity
@Table(indexes = { @Index(name = "unique_post_per_institution", unique = true, columnList = "slug, institution") })
public class InstitutionPost extends Page implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Institution institution;
	
	public Institution getInstitution() {
		return institution;
	}

	public void setInstitution(final Institution institution) {
		this.institution = institution;
	}
}
