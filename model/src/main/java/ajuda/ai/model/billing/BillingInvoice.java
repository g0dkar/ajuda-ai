package ajuda.ai.model.billing;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Pattern.Flag;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

@Entity
@Table(indexes = { @Index(columnList = "user", name = "user") })
public class BillingInvoice implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Column(nullable = false, length = 36)
	@Pattern(regexp = "[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}", flags = { Flag.CASE_INSENSITIVE })
	private String user;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateTime;
	
	@NotBlank
	@Size(max = 0xFF)
	@Column(nullable = false, length = 0xFF)
	private String description;
	
	private String paymentServiceId;
	private int value;
	private boolean paid;
	private int valuePaid;
	private boolean readyForAccounting;
	private boolean cancelled;
	
	@OneToMany(mappedBy = "invoice", orphanRemoval = true, fetch = FetchType.EAGER)
	private List<BillingEvent> events;
	
	/**
	 * Update the accountancy info of this Invoice
	 */
	@PreUpdate
	@PrePersist
	public void beforeSave() {
		valuePaid = 0;
		readyForAccounting = false;
		for (final BillingEvent billingEvent : events) {
			if (billingEvent.getPaymentService().isPaid(billingEvent.getStatus())) {
				valuePaid += billingEvent.getValue();
			}
			
			if (!readyForAccounting) {
				readyForAccounting = billingEvent.getPaymentService().isReadyForAccounting(billingEvent.getStatus());
			}
		}
		
		paid = valuePaid >= value;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(final String user) {
		this.user = user;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(final Date dateTime) {
		this.dateTime = dateTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
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

	public int getValuePaid() {
		return valuePaid;
	}

	public void setValuePaid(final int valuePaid) {
		this.valuePaid = valuePaid;
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

	public List<BillingEvent> getEvents() {
		return events;
	}

	public void setEvents(final List<BillingEvent> events) {
		this.events = events;
	}
}
