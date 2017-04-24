package ajuda.ai.model.billing;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

@Entity
public class PaymentEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Payment payment;
	
	@NotNull
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	
	@NotNull
	@Column(nullable = false, length = 64)
	private String transactionServiceId;
	
	@Min(0)
	@Column(nullable = false)
	private int status;
	
	@Column(length = 8)
	private String currency;
	
	@Min(0)
	@Max(1000000)
	@Column(nullable = false)
	private int value;
	
	@NotBlank
	@Column(length = 16, nullable = false)
	private String paymentType;
	
	@Column(length = 64)
	private String paymentTypeInfo;
	
	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public Payment getPayment() {
		return payment;
	}
	
	public void setPayment(final Payment payment) {
		this.payment = payment;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(final Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getTransactionServiceId() {
		return transactionServiceId;
	}
	
	public void setTransactionServiceId(final String transactionServiceId) {
		this.transactionServiceId = transactionServiceId;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(final int status) {
		this.status = status;
	}
	
	public String getCurrency() {
		return currency;
	}
	
	public void setCurrency(final String currency) {
		this.currency = currency;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(final int value) {
		this.value = value;
	}
	
	public String getPaymentType() {
		return paymentType;
	}
	
	public void setPaymentType(final String paymentType) {
		this.paymentType = paymentType;
	}
	
	public String getPaymentTypeInfo() {
		return paymentTypeInfo;
	}
	
	public void setPaymentTypeInfo(final String paymentTypeInfo) {
		this.paymentTypeInfo = paymentTypeInfo;
	}
}
