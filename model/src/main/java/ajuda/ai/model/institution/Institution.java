package ajuda.ai.model.institution;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Pattern.Flag;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import ajuda.ai.model.Slug;
import ajuda.ai.model.billing.PaymentServiceEnum;
import ajuda.ai.model.extra.CreationInfo;

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
	
	@ElementCollection
	@Column(length = 36)
	@Pattern(regexp = "[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}", flags = { Flag.CASE_INSENSITIVE })
	private Set<String> leaders;
	
	@NotNull
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PaymentServiceEnum paymentService;
	
	@NotBlank
	@Column(nullable = false, length = 1024)
	private String paymentServiceData;

	public CreationInfo getCreation() {
		return creation;
	}

	public void setCreation(final CreationInfo creation) {
		this.creation = creation;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public InstitutionPost getPinnedPost() {
		return pinnedPost;
	}

	public void setPinnedPost(final InstitutionPost pinnedPost) {
		this.pinnedPost = pinnedPost;
	}

	public Set<InstitutionHelper> getHelpers() {
		return helpers;
	}

	public void setHelpers(final Set<InstitutionHelper> helpers) {
		this.helpers = helpers;
	}

	public Set<String> getLeaders() {
		return leaders;
	}

	public void setLeaders(final Set<String> leaders) {
		this.leaders = leaders;
	}

	public PaymentServiceEnum getPaymentService() {
		return paymentService;
	}

	public void setPaymentService(final PaymentServiceEnum paymentService) {
		this.paymentService = paymentService;
	}

	public String getPaymentServiceData() {
		return paymentServiceData;
	}

	public void setPaymentServiceData(final String paymentServiceData) {
		this.paymentServiceData = paymentServiceData;
	}
}