package ajuda.ai.website;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.uol.pagseguro.domain.Sender;
import br.com.uol.pagseguro.domain.paymentrequest.PaymentRequest;
import br.com.uol.pagseguro.domain.paymentrequest.PaymentRequestItem;
import br.com.uol.pagseguro.exception.PagSeguroServiceException;
import br.com.uol.pagseguro.properties.PagSeguroConfig;

public class TestingPaymentGeneration {
	/**
	 * PagSeguro
	 */
	public static void mainPagSeguroEmailPagamento(final String[] args) {
		final PaymentRequest paymentRequest = new PaymentRequest();
		
		final Sender sender = new Sender( //
			"Rafael M. Lins", // name
			"c92273194446662834885@sandbox.pagseguro.com.br" // email
//			"rafael.lins777@gmail.com" // email
		);
		
		paymentRequest.setSender(sender);
		paymentRequest.setName("Teste de Payment Request do PagSeguro");
		paymentRequest.setDescription("Testando essa bagaça");
		
		final PaymentRequestItem item = new PaymentRequestItem(
			null,							// Id
			"Doação para AMA",				// Description
			new BigDecimal("500.00"),		// Price
			1								// Amount
		);
		
		final List<PaymentRequestItem> listItems = new ArrayList<PaymentRequestItem>(1);
		listItems.add(item);
		
		paymentRequest.setItems(listItems);
		
//		final PaymentRequestShipping shipping = new PaymentRequestShipping( //
//			new BigDecimal("5.67") // cost
//		);
//
//		paymentRequest.setShipping(shipping);
		
		paymentRequest.setExpiration(new Integer(30));
		
		// Sets a reference code for this payment request, it's useful to identify this payment in
		// future notifications
		paymentRequest.setReference(UUID.randomUUID().toString());
		
		try {
			final String paymentRequestCode = paymentRequest.register(PagSeguroConfig.getAccountCredentials());
			System.out.println(paymentRequestCode);
		} catch (final PagSeguroServiceException e) {
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * Paypal
	 */
//	public static void mainPayPal(final String[] args) throws Exception {
//		final String clientId = "ATehOF2ClGPWb3k2WQZloqURRK2jaYtwFOKMCpNBimAln2aLxMpITRptYLdDlscMsbVdod2hWvMSs1pl";
//		final String clientSecret = "EMEsK77Pm6uFBE2ByyCi8rAUADZRkcr3MRVtreykvW8LhMWlt4Mq4dEAZ5YeUuNaIWeJjkJUU71Aip41";
//		final Map<String, String> sdkConfig = new HashMap<String, String>();
//		sdkConfig.put("mode", "sandbox");
//
//		final String accessToken = new OAuthTokenCredential(clientId, clientSecret, sdkConfig).getAccessToken();
//		System.out.println("-- Access Token: " + accessToken);
//
//		final APIContext apiContext = new APIContext(accessToken);
//		apiContext.setConfigurationMap(sdkConfig);
//
//		final CreditCard creditCard = new CreditCard();
//		creditCard.setType("mastercard");
//		creditCard.setNumber("5162205816582181");
//		creditCard.setExpireMonth(1);
//		creditCard.setExpireYear(2019);
//		creditCard.setFirstName("Rafael");
//		creditCard.setLastName("M L Araujo");
//
//		final FundingInstrument fundingInstrument = new FundingInstrument();
//		fundingInstrument.setCreditCard(creditCard);
//
//		final List<FundingInstrument> fundingInstrumentList = new ArrayList<FundingInstrument>();
//		fundingInstrumentList.add(fundingInstrument);
//
//		final Payer payer = new Payer();
//		payer.setFundingInstruments(fundingInstrumentList);
//		payer.setPaymentMethod("credit_card");
//
//		final Amount amount = new Amount();
//		amount.setCurrency("USD");
//		amount.setTotal("12");
//
//		final Transaction transaction = new Transaction();
//		transaction.setDescription("creating a direct payment with credit card");
//		transaction.setAmount(amount);
//
//		final List<Transaction> transactions = new ArrayList<Transaction>();
//		transactions.add(transaction);
//
//		final Payment payment = new Payment();
//		payment.setIntent("sale");
//		payment.setPayer(payer);
//		payment.setTransactions(transactions);
//
//		final Payment createdPayment = payment.create(apiContext);
//
//		System.out.println("Payment:");
//		System.out.println("- Cart: " + createdPayment.getCart());
//		System.out.println("- Create Time: " + createdPayment.getCreateTime());
//		System.out.println("- Experience Profile Id: " + createdPayment.getExperienceProfileId());
//		System.out.println("- Failure Reason: " + createdPayment.getFailureReason());
//		System.out.println("- ID: " + createdPayment.getId());
//		System.out.println("- Intent: " + createdPayment.getIntent());
//		System.out.println("- Note To Payer: " + createdPayment.getNoteToPayer());
//		System.out.println("- State: " + createdPayment.getState());
//		System.out.println("- Update Time: " + createdPayment.getUpdateTime());
//		System.out.println("- Billing Agreement Tokens: " + createdPayment.getBillingAgreementTokens());
//		System.out.println("- Failed Transactions: " + createdPayment.getFailedTransactions());
//		System.out.println("- Links: " + createdPayment.getLinks());
//		System.out.println("- Payee: " + createdPayment.getPayee());
//		System.out.println("- Payer: " + createdPayment.getPayer());
//		System.out.println("- Payment Instruction: " + createdPayment.getPaymentInstruction());
//		System.out.println("- Potential Payer Info: " + createdPayment.getPotentialPayerInfo());
//		System.out.println("- Redirect Urls: " + createdPayment.getRedirectUrls());
//		System.out.println("- Transactions: " + createdPayment.getTransactions());
//	}
}
