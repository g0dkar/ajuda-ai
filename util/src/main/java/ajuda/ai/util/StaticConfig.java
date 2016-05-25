package ajuda.ai.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.slf4j.Logger;

import br.com.caelum.vraptor.environment.Environment;

/**
 * Holds some static configuration loaded from properties files in the classpath as especified by {@link Environment}
 * 
 * @author Rafael M. Lins
 *
 */
@ApplicationScoped
public class StaticConfig implements Serializable {
	private static final DateFormat ISO_DATE = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss,zzz'Z'");
	
	private final Logger log;
	private final Environment env;
	
	/** @deprecated CDI */
	@Deprecated StaticConfig() { this(null, null); }
	
	@Inject
	public StaticConfig(final Logger log, final Environment env) {
		this.log = log;
		this.env = env;
	}
	
	/**
	 * Return something from the configurations map.
	 * 
	 * @param path {@code dot.path.to.conf}
	 * @return The configuration value or {@code null} if it doesn't exist
	 */
	public String get(final String path) {
		return env.get(path);
	}
	
	/**
	 * Get a config value.
	 * 
	 * @param path The config path
	 * @param defaultValue Default stuff to return if not found or {@code null}
	 * @return {@link #get(String) The value} or {@code defaultValue}.
	 */
	public <T> T get(final String path, final T defaultValue) {
		try {
			final String value = get(path);
			Object result = null;
			
			if (defaultValue instanceof String) {
				result = value;
			}
			else if (defaultValue instanceof String[]) {
				result = value.split("\\s*,\\s*");
			}
			else if (defaultValue instanceof List) {
				result = Arrays.asList(value.split("\\s*,\\s*"));
			}
			else if (defaultValue instanceof Boolean) {
				result = value != null ? StringUtils.parseBoolean(value) : null;
			}
			else if (defaultValue instanceof Integer) {
				try {
					result = Integer.parseInt(value, 10);
				} catch (final NumberFormatException nfe) {
					if (log.isDebugEnabled()) { log.debug("Error converting to config " + path + " to Integer", nfe); }
					result = null;
				}
			}
			else if (defaultValue instanceof Long) {
				try {
					result = Long.parseLong(value, 10);
				} catch (final NumberFormatException nfe) {
					if (log.isDebugEnabled()) { log.debug("Error converting to config " + path + " to Long", nfe); }
					result = null;
				}
			}
			else if (defaultValue instanceof Double) {
				try {
					result = Double.parseDouble(value);
				} catch (final NumberFormatException nfe) {
					if (log.isDebugEnabled()) { log.debug("Error converting to config " + path + " to Double", nfe); }
					result = null;
				}
			}
			else if (defaultValue instanceof BigDecimal) {
				try {
					result = new BigDecimal(value);
				} catch (final NumberFormatException nfe) {
					if (log.isDebugEnabled()) { log.debug("Error converting to config " + path + " to BigDecimal", nfe); }
					result = null;
				}
			}
			else if (defaultValue instanceof Date) {
				try {
					result = ISO_DATE.parse(value);
				} catch (final ParseException pe) {
					if (log.isDebugEnabled()) { log.debug("Error converting to config " + path + " to Date", pe); }
					result = null;
				}
			}
			else if (defaultValue instanceof Calendar) {
				try {
					final Calendar cal = Calendar.getInstance();
					cal.setTime(ISO_DATE.parse(value));
					result = cal;
				} catch (final ParseException pe) {
					if (log.isDebugEnabled()) { log.debug("Error converting to config " + path + " to Calendar", pe); }
					result = null;
				}
			}
			else {
				if (log.isDebugEnabled()) { log.debug("UnsupportedType: {} (returning defaultValue)", defaultValue.getClass().getName()); }
				return defaultValue;
			}
			
			return result != null ? (T) result : defaultValue;
		} catch (final Exception rte) {
			if (log.isTraceEnabled()) { log.trace("Error while getting something... for some reason (probably a cast error)", rte); }
			return defaultValue;
		}
	}
}
