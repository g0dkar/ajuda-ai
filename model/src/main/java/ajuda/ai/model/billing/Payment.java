package ajuda.ai.model.billing;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import ajuda.ai.model.institution.Helper;
import ajuda.ai.model.institution.Institution;

@Entity
public class Payment implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @Column(length = 32)
	private String id;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Institution institution;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Helper helper;
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	
	@Column(nullable = false)
	private boolean automaticPaidEmailSent;
	
	@NotBlank
	@Size(max = 255)
	@Column(nullable = false, length = 255)
	private String description;
	
	@Column(nullable = false, length = 32)
	@Enumerated(EnumType.STRING)
	private PaymentServiceEnum paymentService;
	
	@Column(length = 64)
	private String paymentServiceId;
	
	@Min(0)
	@Max(1000000)
	@Column(nullable = false)
	private int value;
	
	@Min(0)
	@Max(1000000)
	@Column(nullable = false)
	private int realValue;
	
	@Column(nullable = false)
	private boolean paid;
	
	@Column(nullable = false)
	private boolean readyForAccounting;
	
	@Column(nullable = false)
	private boolean cancelled;
	
	@Size(max = 128)
	@Column(length = 128)
	private String payeeName;
	
	@Email
	@Size(max = 128)
	@Column(length = 128)
	private String payeeEmail;
	
	@OrderBy("timestamp DESC")
	@OneToMany(mappedBy = "payment", orphanRemoval = true, fetch = FetchType.LAZY)
	private List<PaymentEvent> events;
	
	@PrePersist
	public void beforeSave() {
		if (id == null) { id = UUID.randomUUID().toString().replaceAll("-", "").toLowerCase(); }
	}
	
	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public Helper getHelper() {
		return helper;
	}

	public void setHelper(final Helper helper) {
		this.helper = helper;
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

	public boolean isAutomaticPaidEmailSent() {
		return automaticPaidEmailSent;
	}

	public void setAutomaticPaidEmailSent(final boolean automaticPaidEmailSent) {
		this.automaticPaidEmailSent = automaticPaidEmailSent;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(final String payeeName) {
		this.payeeName = payeeName;
	}

	public String getPayeeEmail() {
		return payeeEmail;
	}

	public void setPayeeEmail(final String payeeEmail) {
		this.payeeEmail = payeeEmail;
	}

	public int getRealValue() {
		return realValue;
	}

	public void setRealValue(final int realValue) {
		this.realValue = realValue;
	}
}
