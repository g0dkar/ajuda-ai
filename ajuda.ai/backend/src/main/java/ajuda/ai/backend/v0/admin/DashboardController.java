package ajuda.ai.backend.v0.admin;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;

import ajuda.ai.backend.v0.util.PersistenceService;
import ajuda.ai.model.institution.Institution;
import ajuda.ai.util.StringUtils;
import ajuda.ai.util.keycloak.KeycloakUser;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;

@Controller
@Path("/admin")
public class DashboardController extends AdminController {
	/** @deprecated CDI */ @Deprecated
	DashboardController() { this(null, null, null, null, null, null); }
	
	@Inject
	public DashboardController(final Logger log, final Result result, final PersistenceService ps, final KeycloakUser user, final HttpServletRequest request, final Validator validator) {
		super(log, result, ps, user, request, validator);
	}
	
	@Path("/")
	public void dashboard() {
		
	}
	
	@Path("/sair")
	public void sair() {
		try {
			request.logout();
		} catch (final ServletException e) {
			log.error("Erro ao fazer logoff. Usuário: " + user.getId(), e);
		}
		
		result.redirectTo("https://ajuda.ai/");
	}
	
	@Path("/api/text-to-slug")
	public void textToSlug(final String text) {
		result.use(Results.http()).body(StringUtils.slug(text));
	}
	
	/**
	 * Retorna dados gerais sobre todas as {@link Institution} criadas pelo usuário logado.
	 * 
	 * @param month Mês que o usuário quer ver dados (Jan = 1, Fev = 2, ...)
	 * @param year Ano que o usuário quer ver dados
	 */
	@Path("/api/dashboard-data")
	public void dashboardData(final String month, final String year) {
		final String uid = user.getId();
		final Calendar intervalStart = Calendar.getInstance();
		final Calendar intervalEnd = Calendar.getInstance();
		final Calendar comparisonStart = Calendar.getInstance();
		final Calendar comparisonEnd = Calendar.getInstance();
		
		final int m = StringUtils.parseInteger(month, 0);
		final int y = StringUtils.parseInteger(year, 0);
		
		if (m > 0) {
			intervalStart.set(Calendar.MONTH, m - 1);
			intervalEnd.set(Calendar.MONTH, m - 1);
			comparisonStart.set(Calendar.MONTH, m - 1);
			comparisonEnd.set(Calendar.MONTH, m - 1);
		}
		
		if (y > 0) {
			intervalStart.set(Calendar.YEAR, y);
			intervalEnd.set(Calendar.YEAR, y);
			comparisonStart.set(Calendar.YEAR, y);
			comparisonEnd.set(Calendar.YEAR, y);
		}
		
		intervalStart.set(Calendar.DATE, 1);
		comparisonStart.set(Calendar.DATE, 1);
		comparisonStart.add(Calendar.MONTH, -1);
		
		intervalEnd.set(Calendar.DATE, 1);
		intervalEnd.add(Calendar.MONTH, 1);
		intervalEnd.add(Calendar.DATE, -1);
		
		comparisonEnd.set(Calendar.DATE, 1);
		comparisonEnd.add(Calendar.MONTH, 1);
		comparisonEnd.add(Calendar.DATE, -1);
		comparisonEnd.add(Calendar.MONTH, -1);
		
		final Map<String, Object> result = new HashMap<>(4);
		result.put("institutions", ps.createQuery("SELECT new Map(name as name, slug as slug, paymentService as paymentService, logo as logo, creation as creation) FROM Institution WHERE creator = :creator").setParameter("creator", uid).getResultList());
		result.put("helpers", ps.createQuery("SELECT count(*) FROM Helper h JOIN h.institution i WHERE i.creation.creator = :creator").setParameter("creator", uid).getSingleResult());
		result.put("currentData", donationDataMap(intervalStart, intervalEnd));
		result.put("previousData", donationDataMap(comparisonStart, comparisonEnd));
		jsonResponse(result).serialize();
	}
	
	private Map<String, Object> donationDataMap(final Calendar start, final Calendar end) {
		final String uid = user.getId();
		final Map<String, Object> data = new HashMap<>(7);
		data.put("donationsPerDate", ps.createQuery("SELECT new Map(DATE(p.timestamp) as date, count(p.id) as donations) FROM Payment p JOIN p.institution i WHERE i.creation.creator = :creator AND p.cancelled = false AND DATE(p.timestamp) BETWEEN DATE(:start) AND DATE(:end) GROUP BY DATE(p.timestamp) ORDER BY DATE(p.timestamp) ASC").setParameter("creator", uid).setParameter("start", start.getTime()).setParameter("end", end.getTime()).getResultList());
		data.put("grossDonations", ps.createQuery("SELECT sum(p.value) / 100 FROM Payment p JOIN p.institution i WHERE i.creation.creator = :creator AND p.cancelled = false AND DATE(timestamp) BETWEEN DATE(:start) AND DATE(:end)").setParameter("creator", uid).setParameter("start", start.getTime()).setParameter("end", end.getTime()).getSingleResult());
		data.put("paidDonations", ps.createQuery("SELECT sum(p.value) / 100 FROM Payment p JOIN p.institution i WHERE i.creation.creator = :creator AND p.paid = true AND DATE(timestamp) BETWEEN DATE(:start) AND DATE(:end)").setParameter("creator", uid).setParameter("start", start.getTime()).setParameter("end", end.getTime()).getSingleResult());
		data.put("availableDonations", ps.createQuery("SELECT sum(p.value) / 100 FROM Payment p JOIN p.institution i WHERE i.creation.creator = :creator AND p.readyForAccounting = true AND DATE(timestamp) BETWEEN DATE(:start) AND DATE(:end)").setParameter("creator", uid).setParameter("start", start.getTime()).setParameter("end", end.getTime()).getSingleResult());
		data.put("meanDonation", ps.createQuery("SELECT avg(p.value) / 100 FROM Payment p JOIN p.institution i WHERE i.creation.creator = :creator AND p.paid = true AND DATE(timestamp) BETWEEN DATE(:start) AND DATE(:end)").setParameter("creator", uid).setParameter("start", start.getTime()).setParameter("end", end.getTime()).getSingleResult());
		data.put("cancelledDonations", ps.createQuery("SELECT sum(p.value) / 100 FROM Payment p JOIN p.institution i WHERE i.creation.creator = :creator AND p.cancelled = true AND DATE(timestamp) BETWEEN DATE(:start) AND DATE(:end)").setParameter("creator", uid).setParameter("start", start.getTime()).setParameter("end", end.getTime()).getSingleResult());
		data.put("newHelpers", ps.createQuery("SELECT count(*) FROM Helper h JOIN h.institution i WHERE i.creation.creator = :creator AND DATE(h.timestamp) BETWEEN DATE(:start) AND DATE(:end)").setParameter("creator", uid).setParameter("start", start.getTime()).setParameter("end", end.getTime()).getSingleResult());
		
		return data;
	}
}
