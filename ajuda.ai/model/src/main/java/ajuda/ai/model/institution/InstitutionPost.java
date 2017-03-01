package ajuda.ai.model.institution;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import ajuda.ai.model.extra.Page;

/**
 * Um post feito por uma {@link Institution Instituição} na página da mesma.
 * 
 * @author Rafael Lins - g0dkar
 *
 */
@Entity
public class InstitutionPost extends Page implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Institution institution;
	
	@Column(nullable = false)
	private long pageviews;
	
	public Institution getInstitution() {
		return institution;
	}

	public void setInstitution(Institution institution) {
		this.institution = institution;
	}

	public long getPageviews() {
		return pageviews;
	}

	public void setPageviews(long pageviews) {
		this.pageviews = pageviews;
	}
}
