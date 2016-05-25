package ajuda.ai.model;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
	public void checkReservedSlugs() {
		log.info("Checking reserved slugs...");
		final long start = System.currentTimeMillis();
		final String[] slugs = (HARDCODED_SLUGS + "," + conf.get(RESERVED_SLUGS_CONF, "")).split("\\s*,\\s*");
		
		for (final String fullSlug : slugs) {
			if (fullSlug.length() > 0) {
				final int indexOfSlash = fullSlug.indexOf('/');
				final String directory, slug;
				
				if (indexOfSlash <= 0) {
					slug = fullSlug;
					directory = null;
				} else {
					directory = fullSlug.substring(0, indexOfSlash);
					slug = fullSlug.substring(indexOfSlash + 1);
				}
				
				if (log.isDebugEnabled()) {
					log.debug("Checking for slug: {}/{}", directory, slug);
				}
				final int exists = ((Number) em
						.createQuery("SELECT count(*) FROM Slug WHERE slug = :slug AND directory = :directory")
						.setParameter("slug", slug).setParameter("directory", directory).getSingleResult()).intValue();
				
				if (exists == 0) {
					if (log.isDebugEnabled()) {
						log.debug("Slug doesn't exist: {}/{} - Creating...", directory, slug);
					}
					final int created = em.createQuery("INSERT INTO Slug VALUES (:slug, :directory)")
							.setParameter("slug", slug).setParameter("directory", directory).executeUpdate();
					if (log.isDebugEnabled()) {
						log.debug("Slug doesn't exist: {}/{} - Result: {}", directory, slug, created);
					}
				} else if (log.isDebugEnabled()) {
					log.debug("Slug exists: {}/{}", directory, slug);
				}
			}
		}
		
		log.info("Done checking reserved slugs in {}s", (System.currentTimeMillis() - start) / 1000.0);
	}
}
