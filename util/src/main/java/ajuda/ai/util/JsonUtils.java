package ajuda.ai.util;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.Expose;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * Utility methods for working with JSON. Currently implemented with {@link Gson Google Gson}
 * 
 * @author Rafael Lins
 *
 */
public class JsonUtils {
	/**
	 * GSON converter to and from ISO Dates (for {@link Date} type)
	 * @author Rafael Lins
	 * @see TypeAdapter
	 */
	private static class ISODateJsonConverter extends TypeAdapter<Date> {
		public void write(final JsonWriter out, final Date value) throws IOException {
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(value);
			out.value(value != null ? DatatypeConverter.printDateTime(calendar) : null);
		}
		
		public Date read(final JsonReader in) throws IOException {
			try {
				return DatatypeConverter.parseDateTime(in.nextString()).getTime();
			} catch (final IllegalArgumentException e) {
				return null;
			}
		}
	}
	
	/**
	 * GSON converter to and from ISO Dates (for {@link Calendar} type)
	 * @author Rafael Lins
	 * @see TypeAdapter
	 */
	private static class ISOCalendarJsonConverter extends TypeAdapter<Calendar> {
		public void write(final JsonWriter out, final Calendar value) throws IOException {
			out.value(value != null ? DatatypeConverter.printDateTime(value) : null);
		}
		
		public Calendar read(final JsonReader in) throws IOException {
			try {
				return DatatypeConverter.parseDateTime(in.nextString());
			} catch (final IllegalArgumentException e) {
				return null;
			}
		}
	}
	
	/** {@link Gson} instance pre-built and used by this class. */
	public static final Gson GSON = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.STATIC).registerTypeAdapter(Date.class, new ISODateJsonConverter()).registerTypeAdapter(Calendar.class, new ISOCalendarJsonConverter()).create();
	
	/** {@link Gson} instance pre-built which excludes non-@Exposed annotated fields and used by this class. */
	public static final Gson GSON_EXPOSED = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.STATIC).registerTypeAdapter(Date.class, new ISODateJsonConverter()).registerTypeAdapter(Calendar.class, new ISOCalendarJsonConverter()).create();
	
	/**
	 * Turns an Object into {@link Gson#toJson(Object) json} ignoring {@code static} and fields marked as {@code transient} (by explicitly
	 * including the keyword {@code transient} in their declaration)
	 * 
	 * @param object The object
	 * @return The Json of it
	 * @see Gson#toJson(Object)
	 */
	public static String toJson(final Object object) {
		return GSON.toJson(object);
	}
	
	/**
	 * Turns an Object into {@link Gson#toJson(Object) json} ignoring {@code static}, fields marked as {@code transient} (by explicitly
	 * including the keyword {@code transient} in their declaration) and any field NOT annotated with {@link Expose}
	 * 
	 * @param object The object
	 * @return The Json of it
	 * @see Gson#toJson(Object)
	 * @see Expose
	 */
	public static String toJsonExposed(final Object object) {
		return GSON_EXPOSED.toJson(object);
	}
	
	/**
	 * {@link Gson#fromJson(String, Class) Turns} a Json string back to an object
	 * @param json The Json String
	 * @param klass The final Object {@link Class}
	 * @return The object built from the Json Map
	 * @throws JsonParseException Invalid/malformed Json
	 */
	public static <T> T fromJson(final String json, final Class<T> klass) {
		return GSON.fromJson(json, klass);
	}
	
	/**
	 * Turns a Json into a {@link Map}
	 * @param json The Json
	 * @return The Map
	 */
	public static Map<String, Object> asJsonMap(final String json) {
		return GSON.fromJson(json, Map.class);
	}
	
	/**
	 * Builds a Json Map for a {@link Throwable} object.
	 * @param throwable The Throwable
	 * @return A Json Map with the {@link Throwable} class name and it's {@link Throwable#getMessage() message}.
	 */
	public static Map<String, Object> asJsonMap(final Throwable throwable) {
		return asJsonMap(throwable, false);
	}
	
	/**
	 * Builds a Json Map for a {@link Throwable} object. Optionally, recursively including it's {@link Throwable#getCause() stack trace}.
	 * @param throwable The Throwable
	 * @param withStacktrace Include the {@link Throwable#getCause() stack trace}?
	 * @return A Json Map with the {@link Throwable} class name and it's {@link Throwable#getMessage() message}.
	 */
	public static Map<String, Object> asJsonMap(final Throwable throwable, final boolean withStacktrace) {
		final Map<String, Object> json = new HashMap<String, Object>(withStacktrace && throwable.getCause() != null ? 3 : 2);
		json.put("exception", throwable.getClass().getName());
		json.put("message", throwable.getMessage());
		
		if (withStacktrace && throwable.getCause() != null) {
			json.put("cause", asJsonMap(throwable.getCause(), true));
		}
		
		return json;
	}
}
