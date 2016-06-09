package ajuda.ai.model.billing;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.caelum.vraptor.serialization.SkipSerialization;

@Entity
public class BillingEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@SkipSerialization
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private BillingInvoice invoice;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	
	private String transactionServiceId;
	private String clientId;
	private String clientName;
	private String clientEmail;
	private int status;
	private String currency;
	private int value;
	private String paymentType;
	
	@Enumerated(EnumType.STRING)
	private PaymentServiceEnum paymentService;
	
	public Long getId() {
		return id;
	}
	
	public void setId(final Long id) {
		this.id = id;
	}
	
	public String getTransactionServiceId() {
		return transactionServiceId;
	}
	
	public void setTransactionServiceId(final String transactionServiceId) {
		this.transactionServiceId = transactionServiceId;
	}
	
	public String getClientId() {
		return clientId;
	}
	
	public void setClientId(final String clientId) {
		this.clientId = clientId;
	}
	
	public String getClientName() {
		return clientName;
	}
	
	public void setClientName(final String clientName) {
		this.clientName = clientName;
	}
	
	public String getClientEmail() {
		return clientEmail;
	}
	
	public void setClientEmail(final String clientEmail) {
		this.clientEmail = clientEmail;
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
	
	public PaymentServiceEnum getPaymentService() {
		return paymentService;
	}
	
	public void setPaymentService(final PaymentServiceEnum paymentService) {
		this.paymentService = paymentService;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(final Date timestamp) {
		this.timestamp = timestamp;
	}

	public BillingInvoice getInvoice() {
		return invoice;
	}

	public void setInvoice(final BillingInvoice invoice) {
		this.invoice = invoice;
	}
}
