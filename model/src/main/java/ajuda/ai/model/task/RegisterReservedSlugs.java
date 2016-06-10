package ajuda.ai.model.task;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;

import ajuda.ai.util.Config;

/**
 * Esta classe faz uma verificação se alguns slugs reservados existem. Se não existirem, ela os
 * cria. Isso evita que criem slugs como "api" ou "auth", ambos usados pelo sistema.
 * 
 * @author Rafael Lins - g0dkar
 *
 */
@Startup
@Singleton
public class RegisterReservedSlugs {
	private static final String RESERVED_SLUGS_CONF = "reserved_slugs";
	private static final String HARDCODED_SLUGS = "api,auth";
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private Logger log;
	
	@Inject
	private Config conf;
	
	@PostConstruct
	@Transactional
	public void checkReservedSlugs() {
		log.info("Checking reserved slugs...");
		final long start = System.currentTimeMillis();
		final String[] slugs = (HARDCODED_SLUGS + "," + conf.get(RESERVED_SLUGS_CONF, "")).split("\\s*,\\s*");
		
		for (final String slug : slugs) {
			if (slug.length() > 0) {
				if (log.isDebugEnabled()) {
					log.debug("Checking for slug: /{}", slug);
				}
				final int exists = ((Number) em
						.createQuery("SELECT count(*) FROM Slug WHERE slug = :slug")
						.setParameter("slug", slug).getSingleResult()).intValue();
				
				if (exists == 0) {
					if (log.isDebugEnabled()) {
						log.debug("Slug doesn't exist: /{} - Creating...", slug);
					}
					
					final int created = em.createNativeQuery("INSERT INTO slug(`slug`) VALUES (:slug)")
							.setParameter("slug", slug).executeUpdate();
					
					if (log.isDebugEnabled()) {
						log.debug("Slug doesn't exist: /{} - INSERT Result: {}", slug, created);
					}
				} else if (log.isDebugEnabled()) {
					log.debug("Slug exists: /{}", slug);
				}
			}
		}
		
		log.info("Done checking reserved slugs in {}s", (System.currentTimeMillis() - start) / 1000.0);
	}
}
