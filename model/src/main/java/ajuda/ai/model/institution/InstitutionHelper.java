package ajuda.ai.model.institution;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import ajuda.ai.model.billing.Payment;

/**
 * Representa uma pessoa que está ajudando uma {@link Institution Instituição}
 * 
 * @author Rafael Lins - g0dkar
 *
 */
@Entity
@Table(indexes = { @Index(name = "unique_email_per_institution", columnList = "institution, email", unique = true) })
public class InstitutionHelper implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Institution institution;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date timestamp;
	
	@Size(max = 128)
	@Column(length = 128)
	private String name;
	
	@Email
	@NotBlank
	@Size(max = 128)
	@Column(nullable = false, length = 128)
	private String email;
	
	@Email
	@Size(max = 128)
	@Column(length = 128)
	private String paymentEmail;
	
	@Column(nullable = false)
	private boolean anonymous;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Payment lastPayment;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Institution getInstitution() {
		return institution;
	}

	public void setInstitution(final Institution institution) {
		this.institution = institution;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(final Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getPaymentEmail() {
		return paymentEmail;
	}

	public void setPaymentEmail(final String paymentEmail) {
		this.paymentEmail = paymentEmail;
	}

	public boolean isAnonymous() {
		return anonymous;
	}

	public void setAnonymous(final boolean anonymous) {
		this.anonymous = anonymous;
	}

	public Payment getLastPayment() {
		return lastPayment;
	}

	public void setLastPayment(final Payment lastPayment) {
		this.lastPayment = lastPayment;
	}
}
