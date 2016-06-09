package ajuda.ai.model.billing;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import ajuda.ai.model.institution.Institution;
import ajuda.ai.model.institution.InstitutionHelp;

@Entity
@Table(indexes = { @Index(columnList = "user", name = "user") })
public class Payment implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Institution institution;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private InstitutionHelp help;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	
	@NotBlank
	@Size(max = 0xFF)
	@Column(nullable = false, length = 0xFF)
	private String description;
	
	@Enumerated(EnumType.STRING)
	private PaymentServiceEnum paymentService;
	
	@NotBlank
	@Column(nullable = false, length = 64)
	private String paymentServiceId;
	
	@Min(0)
	@Max(1000000)
	@Column(nullable = false)
	private int value;
	
	@Column(nullable = false)
	private boolean paid;
	
	@Column(nullable = false)
	private boolean readyForAccounting;
	
	@Column(nullable = false)
	private boolean cancelled;
	
	@OrderBy("timestamp DESC")
	@OneToMany(mappedBy = "payment", orphanRemoval = true, fetch = FetchType.LAZY)
	private List<PaymentEvent> events;
	
	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public InstitutionHelp getHelp() {
		return help;
	}

	public void setHelp(final InstitutionHelp help) {
		this.help = help;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(final Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public PaymentServiceEnum getPaymentService() {
		return paymentService;
	}

	public void setPaymentService(final PaymentServiceEnum paymentService) {
		this.paymentService = paymentService;
	}

	public String getPaymentServiceId() {
		return paymentServiceId;
	}

	public void setPaymentServiceId(final String paymentServiceId) {
		this.paymentServiceId = paymentServiceId;
	}

	public int getValue() {
		return value;
	}

	public void setValue(final int value) {
		this.value = value;
	}

	public boolean isPaid() {
		return paid;
	}

	public void setPaid(final boolean paid) {
		this.paid = paid;
	}

	public boolean isReadyForAccounting() {
		return readyForAccounting;
	}

	public void setReadyForAccounting(final boolean readyForAccounting) {
		this.readyForAccounting = readyForAccounting;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(final boolean cancelled) {
		this.cancelled = cancelled;
	}

	public List<PaymentEvent> getEvents() {
		return events;
	}

	public void setEvents(final List<PaymentEvent> events) {
		this.events = events;
	}

	public Institution getInstitution() {
		return institution;
	}

	public void setInstitution(final Institution institution) {
		this.institution = institution;
	}
}
