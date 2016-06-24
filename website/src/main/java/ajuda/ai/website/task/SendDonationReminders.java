//package ajuda.ai.website.task;
//
//import java.util.LinkedList;
//import java.util.List;
//
//import javax.ejb.Schedule;
//import javax.inject.Inject;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.Query;
//import javax.transaction.Transactional;
//
//import org.hibernate.annotations.QueryHints;
//import org.slf4j.Logger;
//
//import ajuda.ai.model.billing.PaymentServiceEnum;
//import ajuda.ai.model.institution.InstitutionHelper;
//import ajuda.ai.website.util.ReminderMailSender;
//
///**
// * Envia semanalmente os lembretes de doação
// *
// * @author Rafael Lins
// *
// */
//public class SendDonationReminders {
//	@Inject
//	private Logger log;
//
//	@PersistenceContext
//	private EntityManager entityManager;
//
//	@Inject
//	private ReminderMailSender mailSender;
//
//	@Transactional
//	@Schedule(hour = "6", dayOfWeek = "Mon")		// Segunda, 6h da manhã
//	public void run() {
//		log.info("Enviando lembretes de doação...");
//
//		final long start = System.currentTimeMillis();
//		final long count = ((Number) entityManager.createQuery("SELECT count(*) FROM InstitutionHelper WHERE rememberToken IS NOT NULL").getSingleResult()).longValue();
//		log.info("Lembretes para enviar: {} (tempo: {}s)", count, tempo(start));
//
//		final Query query = entityManager.createQuery("FROM InstitutionHelper WHERE rememberToken IS NOT NULL").setParameter("paymentService", PaymentServiceEnum.PAG_SEGURO);
//		query.setHint(QueryHints.FETCH_SIZE, 100);	// Pega de 100 em 100 resultados de forma transparente
//		query.setHint(QueryHints.READ_ONLY, true);	// Updates serão feitos de forma manual (e assim é mais rápido)
//		final List<InstitutionHelper> helpers = query.getResultList();
//		int enviados = 0, erros = 0;
//		final List<Long> remove = new LinkedList<>();
//
//		for (final InstitutionHelper helper : helpers) {
//			try {
//				final boolean enviado = mailSender.sendReminder(helper, false);
//
//				if (enviado) {
//					enviados++;
//				}
//				else {
//					remove.add(helper.getId());
//				}
//			} catch (final Exception e) {
//				erros++;
//				log.error("Erro enviando lembrete de doação", e);
//			}
//		}
//
//		entityManager.createQuery("UPDATE InstitutionHelper SET reminderToken = NULL, reminderTokenDate = NULL WHERE id IN (:ids)").setParameter("ids", remove).executeUpdate();
//		log.info("Lembretes enviados: {}, Removidos: {}, Erros: {} (tempo: {}s)", enviados, remove.size(), erros, tempo(start));
//	}
//
//	private double tempo(final long start) {
//		return System.currentTimeMillis() - start / 1000.0;
//	}
//}
