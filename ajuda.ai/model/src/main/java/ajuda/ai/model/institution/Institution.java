package ajuda.ai.model.institution;

import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKeyColumn;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import com.google.gson.annotations.Expose;

import ajuda.ai.model.Slug;
import ajuda.ai.model.billing.PaymentServiceEnum;
import ajuda.ai.model.extra.CreationInfo;
import ajuda.ai.util.JsonUtils;
import ajuda.ai.util.StringUtils;

/**
 * Representa uma Instituição que será ajudada por seus {@link Helper Ajudantes}
 * 
 * @author Rafael Lins - g0dkar
 *
 */
@Entity
public class Institution extends Slug {
	/** Serial Version UID */
	private static final long serialVersionUID = 1L;
	
	@Expose
	@Embedded
	private CreationInfo creation;
	
	@Expose
	@NotBlank
	@Size(max = 64)
	@Column(nullable = false, length = 64)
	private String name;
	
	@Expose
	@NotBlank
	@Size(max = 65535)
	@Column(nullable = false, columnDefinition = "MEDIUMTEXT")
	private String description;
	
	/**
	 * Para o futuro: Lista de usuários que poderão gerenciar essa instituição
	 */
	@Column(length = 36)
	@ElementCollection(fetch = FetchType.LAZY)
	private Set<String> leaders;
	
	@Expose
	@NotNull
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PaymentServiceEnum paymentService;
	
	@URL
	@Expose
	@Column(length = 1024)
	private String logo;
	
	@URL
	@Expose
	@Column(length = 1024)
	private String banner;
	
	@Column(name = "value", length = 512)
	@ElementCollection(fetch = FetchType.EAGER)
	@MapKeyColumn(name = "attribute", length = 24)
	@JoinTable(name = "institution_attributes", joinColumns = @JoinColumn(name = "id"))
	private Map<String, String> attributes;

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
	
	public String getDescriptionMarkdown() {
		return StringUtils.markdown(description);
	}

	public void setDescription(final String description) {
		this.description = description;
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

	public String getLogo() {
		return logo;
	}

	public void setLogo(final String logo) {
		this.logo = logo;
	}

	public String getBanner() {
		return banner;
	}

	public void setBanner(final String banner) {
		this.banner = banner;
	}
	
	public String toJson() {
		return JsonUtils.toJsonExposed(this);
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}
	
	public String attributeMarkdown(final String attribute) {
		return StringUtils.markdown(attributes.get(attribute));
	}

	public void setAttributes(final Map<String, String> attributes) {
		this.attributes = attributes;
	}
}