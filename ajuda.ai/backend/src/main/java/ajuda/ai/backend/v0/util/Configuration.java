//package ajuda.ai.backend.v0.util;
//
//import java.math.BigDecimal;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//import javax.enterprise.context.RequestScoped;
//import javax.inject.Inject;
//import javax.persistence.Query;
//
//import org.infinispan.Cache;
//import org.slf4j.Logger;
//
//import ajuda.ai.util.StringUtils;
//
//@RequestScoped
//public class Configuration {
//	private static final DateFormat ISO_DATE = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss,zzz'Z'");
//	
//	private final Logger log;
//	private final PersistenceService ps;
//	private final CacheService cs;
//	
//	/** @deprecated CDI */ @Deprecated
//	Configuration() { this(null, null, null); }
//	
//	@Inject
//	public Configuration(final PersistenceService ps, final CacheService cs, final Logger log) {
//		this.ps = ps;
//		this.cs = cs;
//		this.log = log;
//	}
//	
//	/**
//	 * Return something from the configurations map.
//	 * 
//	 * @param name {@code dot.path.to.conf}
//	 * @return The configuration value or {@code null} if it doesn't exist
//	 */
//	public String get(final String name) {
//		final Cache<String, String> cache = cs.getCache("configuration");
//		
//		if (!cache.containsKey(name)) {
//			if (log.isDebugEnabled()) { log.debug("Cache Miss: {}, Loading...", name); }
//			final Query query = ps.createQuery("SELECT value FROM Configuration WHERE name = :name").setParameter("name", name);
//			query.setHint("org.hibernate.cacheable", false);
//			
//			final String value = (String) query.getSingleResult();
//			if (value != null) {
//				cache.put(name, value);
//			}
//			
//			if (log.isDebugEnabled()) { log.debug("Loaded into Cache: {} = {}", name, value); }
//		}
//		
//		return cache.get(name);
//	}
//	
//	/**
//	 * Get a config value.
//	 * 
//	 * @param path The config path
//	 * @param defaultValue Default stuff to return if not found or {@code null}
//	 * @return {@link #get(String) The value} or {@code defaultValue}.
//	 */
//	public <T> T get(final String path, final T defaultValue) {
//		try {
//			final String value = get(path);
//			
//			if (value != null) {
//				Object result = null;
//				
//				if (defaultValue instanceof String) {
//					result = value;
//				}
//				else if (defaultValue instanceof String[]) {
//					result = value.split("\\s*,\\s*");
//				}
//				else if (defaultValue instanceof List) {
//					result = Arrays.asList(value.split("\\s*,\\s*"));
//				}
//				else if (defaultValue instanceof Boolean) {
//					result = value != null ? StringUtils.parseBoolean(value) : null;
//				}
//				else if (defaultValue instanceof Integer) {
//					try {
//						result = Integer.parseInt(value, 10);
//					} catch (final NumberFormatException nfe) {
//						if (log.isDebugEnabled()) { log.debug("Error converting to config " + path + " to Integer", nfe); }
//						result = null;
//					}
//				}
//				else if (defaultValue instanceof Long) {
//					try {
//						result = Long.parseLong(value, 10);
//					} catch (final NumberFormatException nfe) {
//						if (log.isDebugEnabled()) { log.debug("Error converting to config " + path + " to Long", nfe); }
//						result = null;
//					}
//				}
//				else if (defaultValue instanceof Double) {
//					try {
//						result = Double.parseDouble(value);
//					} catch (final NumberFormatException nfe) {
//						if (log.isDebugEnabled()) { log.debug("Error converting to config " + path + " to Double", nfe); }
//						result = null;
//					}
//				}
//				else if (defaultValue instanceof BigDecimal) {
//					try {
//						result = new BigDecimal(value);
//					} catch (final NumberFormatException nfe) {
//						if (log.isDebugEnabled()) { log.debug("Error converting to config " + path + " to BigDecimal", nfe); }
//						result = null;
//					}
//				}
//				else if (defaultValue instanceof Date) {
//					try {
//						result = ISO_DATE.parse(value);
//					} catch (final ParseException pe) {
//						if (log.isDebugEnabled()) { log.debug("Error converting to config " + path + " to Date", pe); }
//						result = null;
//					}
//				}
//				else if (defaultValue instanceof Calendar) {
//					try {
//						final Calendar cal = Calendar.getInstance();
//						cal.setTime(ISO_DATE.parse(value));
//						result = cal;
//					} catch (final ParseException pe) {
//						if (log.isDebugEnabled()) { log.debug("Error converting to config " + path + " to Calendar", pe); }
//						result = null;
//					}
//				}
//				else {
//					if (log.isDebugEnabled()) { log.debug("UnsupportedType: {} (returning defaultValue)", defaultValue.getClass().getName()); }
//					return defaultValue;
//				}
//				
//				return result != null ? (T) result : defaultValue;
//			}
//			else {
//				return defaultValue;
//			}
//		} catch (final Exception e) {
//			if (log.isTraceEnabled()) { log.trace("Error while getting Configuration... for some reason (probably a cast error)", e); }
//			return defaultValue;
//		}
//	}
//}
