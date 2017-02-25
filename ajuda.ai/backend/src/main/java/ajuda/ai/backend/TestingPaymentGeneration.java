package ajuda.ai.backend;

public class TestingPaymentGeneration {
//	public static void main(final String[] args) throws IOException {
//		final String moipEndpoint = "https://www.moip.com.br/PagamentoMoIP.do";
//		final URL url = new URL(moipEndpoint);
//		final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//		conn.setDoOutput(true);
//		conn.getOutputStream().write("id_carteira=rafael.lins777@gmail.com&valor=1000&nome=Doacao+AMA&id_transacao=7&pagador_nome=Rafael+Lins".getBytes());
//	}
	
//	public static void mainPagSeguroCheckout(final String[] args) {
//		final Institution institution = new Institution();
//		institution.setId(7L);
//		institution.setName("AMA - Associação dos Amigos dos Autistas");
//		institution.setSlug("ama");
//
//		final int value = 12300;
//
//		final String paymentDescription = "Doação: " + institution.getName();
//		final BigDecimal valueBigDecimal = new BigDecimal(value).divide(new BigDecimal(100)).setScale(2);
//
//		System.out.println(value);
//		System.out.println(valueBigDecimal);
//
//		final Checkout checkout = new Checkout();
//		checkout.addItem(institution.getId().toString(), paymentDescription, 1, valueBigDecimal, null, null);
//		checkout.setCurrency(Currency.BRL);
//		checkout.setReference(institution.getId() + "-" + 123);
//		checkout.setRedirectURL("https://ajuda.ai/" + institution.getSlug() + "/obrigado");
//		checkout.setNotificationURL("https://ajuda.ai/" + institution.getSlug() + "/api/transaction-notification");
//
//		try {
//			final boolean onlyCheckoutCode = false;
//			final String response = checkout.register(PagSeguroConfig.getAccountCredentials(), onlyCheckoutCode);
//			final Matcher codeMatcher = Pattern.compile("code=([A-F0-9]+)").matcher(response);
//			final String pagSeguroId = codeMatcher.find() ? codeMatcher.group(1) : null;
//
//			System.out.println(response);
//			System.out.println(pagSeguroId);
//
////			final Payment payment = new Payment();
////			payment.setInstitution(institution);
////			payment.setHelper(institutionHelper);
////			payment.setPaymentServiceId(pagSeguroId);
////			payment.setDescription("Pagamento criado via notificação de pagamento do PagSeguro");
////			payment.setPaymentService(institution.getPaymentService());
////			payment.setTimestamp(new Date());
////			payment.setValue(value);
////			payment.setPaid(false);
////			payment.setCancelled(false);
////			payment.setReadyForAccounting(false);
////
////			result.redirectTo(response);
////			return payment;
//		} catch (final PagSeguroServiceException e) {
//			System.err.println(e.getMessage());
//		}
//	}
//
//	public static void mainPagSeguroListagemPagamentos(final String[] args) {
//		TransactionSearchResult transactionSearchResult = null;
//
//		try {
//			final Calendar monthAgo = Calendar.getInstance();
//			monthAgo.add(Calendar.DATE, -30);
//
//			final Calendar now = Calendar.getInstance();
//
//			final int page = 1;
//
//			final int maxPageResults = 1000;
//
//			transactionSearchResult = TransactionSearchService.searchByDate(PagSeguroConfig.getAccountCredentials(),
//					monthAgo.getTime(), now.getTime(), page, maxPageResults);
//
//		} catch (final PagSeguroServiceException e) {
//			System.err.println(e.getMessage());
//		}
//
//		if (transactionSearchResult != null) {
//			final List<TransactionSummary> listTransactionSummaries = transactionSearchResult.getTransactionSummaries();
//			final Iterator<TransactionSummary> transactionSummariesIterator = listTransactionSummaries.iterator();
//
//			while (transactionSummariesIterator.hasNext()) {
//				final TransactionSummary currentTransactionSummary = transactionSummariesIterator.next();
//				System.out.println();
//				System.out.println("code: " + currentTransactionSummary.getCode());
//				System.out.println("date: " + currentTransactionSummary.getDate());
//				System.out.println("reference: " + currentTransactionSummary.getReference());
//				System.out.println("status: " + currentTransactionSummary.getStatus());
//				System.out.println("lastEventDate: " + currentTransactionSummary.getLastEvent());
//			}
//		}
//	}
//	/**
//	 * PagSeguro
//	 */
//	public static void mainPagSeguroGerarPagamento(final String[] args) {
//		final PaymentRequest paymentRequest = new PaymentRequest();
//
//		final Sender sender = new Sender( //
//			"Rafael M. Lins", // name
//			"c92273194446662834885@sandbox.pagseguro.com.br" // email
////			"rafael.lins777@gmail.com" // email
//		);
//
//		paymentRequest.setSender(sender);
//		paymentRequest.setName("Teste de Payment Request do PagSeguro");
//		paymentRequest.setDescription("Testando essa bagaça");
//
//		final PaymentRequestItem item = new PaymentRequestItem(
//			null,							// Id
//			"Teste de Payment Request",		// Description
//			new BigDecimal("500.00"),		// Price
//			1								// Amount
//		);
//
//		final List<PaymentRequestItem> listItems = new ArrayList<PaymentRequestItem>(1);
//		listItems.add(item);
//
//		paymentRequest.setItems(listItems);
//
////		final PaymentRequestShipping shipping = new PaymentRequestShipping( //
////			new BigDecimal("5.67") // cost
////		);
////
////		paymentRequest.setShipping(shipping);
//
//		paymentRequest.setExpiration(new Integer(30));
//
//		// Sets a reference code for this payment request, it's useful to identify this payment in
//		// future notifications
//		paymentRequest.setReference(UUID.randomUUID().toString());
//
//		try {
//			final String paymentRequestCode = paymentRequest.register(PagSeguroConfig.getAccountCredentials());
//			System.out.println(paymentRequestCode);
//		} catch (final PagSeguroServiceException e) {
//			System.err.println(e.getMessage());
//		}
//	}
	
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
