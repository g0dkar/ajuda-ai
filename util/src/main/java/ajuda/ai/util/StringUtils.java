package ajuda.ai.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.markdownj.MarkdownProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Métodos utilitários para se trabalhar com Strings.
 * 
 * @author g0dkar
 *
 */
public class StringUtils {
	private static final Logger log = LoggerFactory.getLogger(StringUtils.class);
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	public static Map<String, String> HTML_ASCII_TABLE;
	
	static {
		HTML_ASCII_TABLE = new LinkedHashMap<String, String>(95);	// A ordem das chaves é importante
		
		// Este *TEM* que ser o primeiro!
		HTML_ASCII_TABLE.put("&", "&amp;");
		
		HTML_ASCII_TABLE.put(" ", "&nbsp;");
		HTML_ASCII_TABLE.put("“", "&ldquo;");
		HTML_ASCII_TABLE.put("�?", "&rdquo;");
		HTML_ASCII_TABLE.put("\"", "&quot;");
		HTML_ASCII_TABLE.put("<", "&lt;");
		HTML_ASCII_TABLE.put(">", "&gt;");
		
		HTML_ASCII_TABLE.put("¡", "&iexcl;");
		HTML_ASCII_TABLE.put("¢", "&cent;");
		HTML_ASCII_TABLE.put("£", "&pound;");
		HTML_ASCII_TABLE.put("¤", "&curren;");
		HTML_ASCII_TABLE.put("¥", "&yen;");
		HTML_ASCII_TABLE.put("¦", "&brvbar;");
		HTML_ASCII_TABLE.put("§", "&sect;");
		HTML_ASCII_TABLE.put("¨", "&uml;");
		HTML_ASCII_TABLE.put("©", "&copy;");
		HTML_ASCII_TABLE.put("ª", "&ordf;");
		HTML_ASCII_TABLE.put("«", "&laquo;");
		HTML_ASCII_TABLE.put("¬", "&not;");
		HTML_ASCII_TABLE.put("®", "&reg;");
		HTML_ASCII_TABLE.put("¯", "&macr;");
		HTML_ASCII_TABLE.put("°", "&deg;");
		HTML_ASCII_TABLE.put("±", "&plusmn;");
		HTML_ASCII_TABLE.put("²", "&sup2;");
		HTML_ASCII_TABLE.put("³", "&sup3;");
		HTML_ASCII_TABLE.put("´", "&acute;");
		HTML_ASCII_TABLE.put("µ", "&micro;");
		HTML_ASCII_TABLE.put("¶", "&para;");
		HTML_ASCII_TABLE.put("·", "&middot;");
		HTML_ASCII_TABLE.put("¸", "&cedil;");
		HTML_ASCII_TABLE.put("¹", "&sup1;");
		HTML_ASCII_TABLE.put("º", "&ordm;");
		HTML_ASCII_TABLE.put("»", "&raquo;");
		HTML_ASCII_TABLE.put("¼", "&frac14;");
		HTML_ASCII_TABLE.put("½", "&frac12;");
		HTML_ASCII_TABLE.put("¾", "&frac34;");
		HTML_ASCII_TABLE.put("¿", "&iquest;");
		HTML_ASCII_TABLE.put("á", "&aacute;");
		HTML_ASCII_TABLE.put("�?", "&Aacute;");
		HTML_ASCII_TABLE.put("à", "&agrave;");
		HTML_ASCII_TABLE.put("À", "&Agrave;");
		HTML_ASCII_TABLE.put("â", "&acirc;");
		HTML_ASCII_TABLE.put("Â", "&Acirc;");
		HTML_ASCII_TABLE.put("ä", "&auml;");
		HTML_ASCII_TABLE.put("Ä", "&Auml;");
		HTML_ASCII_TABLE.put("ã", "&atilde;");
		HTML_ASCII_TABLE.put("Ã", "&Atilde;");
		HTML_ASCII_TABLE.put("å", "&aring;");
		HTML_ASCII_TABLE.put("Å", "&Aring;");
		HTML_ASCII_TABLE.put("ç", "&ccedil;");
		HTML_ASCII_TABLE.put("Ç", "&Ccedil;");
		HTML_ASCII_TABLE.put("æ", "&aelig;");
		HTML_ASCII_TABLE.put("Æ", "&AElig;");
		HTML_ASCII_TABLE.put("é", "&eacute;");
		HTML_ASCII_TABLE.put("É", "&Eacute;");
		HTML_ASCII_TABLE.put("è", "&egrave;");
		HTML_ASCII_TABLE.put("È", "&Egrave;");
		HTML_ASCII_TABLE.put("ë", "&euml;");
		HTML_ASCII_TABLE.put("Ë", "&Euml;");
		HTML_ASCII_TABLE.put("ê", "&ecirc;");
		HTML_ASCII_TABLE.put("Ê", "&Ecirc;");
		HTML_ASCII_TABLE.put("í", "&iacute;");
		HTML_ASCII_TABLE.put("�?", "&Iacute;");
		HTML_ASCII_TABLE.put("ì", "&igrave;");
		HTML_ASCII_TABLE.put("Ì", "&Igrave;");
		HTML_ASCII_TABLE.put("î", "&icirc;");
		HTML_ASCII_TABLE.put("Î", "&Icirc;");
		HTML_ASCII_TABLE.put("ï", "&iuml;");
		HTML_ASCII_TABLE.put("�?", "&Iuml;");
		HTML_ASCII_TABLE.put("ó", "&oacute;");
		HTML_ASCII_TABLE.put("Ó", "&Oacute;");
		HTML_ASCII_TABLE.put("ò", "&ograve;");
		HTML_ASCII_TABLE.put("Ò", "&Ograve;");
		HTML_ASCII_TABLE.put("ô", "&ocirc;");
		HTML_ASCII_TABLE.put("Ô", "&Ocirc;");
		HTML_ASCII_TABLE.put("ö", "&ouml;");
		HTML_ASCII_TABLE.put("Ö", "&Ouml;");
		HTML_ASCII_TABLE.put("õ", "&otilde;");
		HTML_ASCII_TABLE.put("Õ", "&Otilde;");
		HTML_ASCII_TABLE.put("ú", "&uacute;");
		HTML_ASCII_TABLE.put("Ú", "&Uacute;");
		HTML_ASCII_TABLE.put("ù", "&ugrave;");
		HTML_ASCII_TABLE.put("Ù", "&Ugrave;");
		HTML_ASCII_TABLE.put("û", "&ucirc;");
		HTML_ASCII_TABLE.put("Û", "&Ucirc;");
		HTML_ASCII_TABLE.put("ü", "&uuml;");
		HTML_ASCII_TABLE.put("Ü", "&Uuml;");
		HTML_ASCII_TABLE.put("×", "&times;");
		HTML_ASCII_TABLE.put("ø", "&oslash;");
		HTML_ASCII_TABLE.put("Ø", "&Oslash;");
		HTML_ASCII_TABLE.put("ñ", "&ntilde;");
		HTML_ASCII_TABLE.put("Ñ", "&Ntilde;");
		HTML_ASCII_TABLE.put("ý", "&yacute;");
		HTML_ASCII_TABLE.put("�?", "&Yacute;");
		HTML_ASCII_TABLE.put("ÿ", "&yuml;");
		HTML_ASCII_TABLE.put("þ", "&thorn;");
		HTML_ASCII_TABLE.put("Þ", "&THORN;");
		HTML_ASCII_TABLE.put("÷", "&divide;");
		HTML_ASCII_TABLE.put("€", "&euro;");
	}
	
	/**
	 * Substitui caracteres especiais pelos seus correspondentes códigos HTML
	 * 
	 * @param str String para codificar
	 * @return String codificada
	 */
	public static String encodeHTML(String str) {
		if (!isBlank(str)) {
			for (final Entry<String, String> entry : HTML_ASCII_TABLE.entrySet()) {
				if (!entry.getValue().equals("&nbsp;")) {
					str = str.replaceAll(entry.getKey(), entry.getValue());
				}
			}
		}
		
		return str;
	}
	
	/**
	 * Substitui códigos HTML pelos seus caracteres especiais correspondentes
	 * 
	 * @param str String para codificar
	 * @return String codificada
	 */
	public static String decodeHTML(String str) {
		if (!isBlank(str)) {
			for (final Entry<String, String> entry : HTML_ASCII_TABLE.entrySet()) {
				if (!entry.getValue().equals("&nbsp;")) {
					str = str.replaceAll(entry.getValue(), entry.getKey());
				}
			}
		}
		
		return str;
	}
	
	/**
	 * Codifica uma String como componente de URL
	 * 
	 * @param urlComponent String do componente URL
	 * @return String codificada como componente URL
	 */
	public static String encodeURLComponent(final String urlComponent) {
		return encodeURLComponent(urlComponent, "UTF-8");
	}
	
	/**
	 * Codifica uma String como componente de URL no encoding especificado
	 * 
	 * @param urlComponent String do componente URL
	 * @param encoding Encoding especificado (recomendado: {@code "UTF-8"})
	 * 
	 * @return String codificada como componente URL no encoding especificado
	 */
	public static String encodeURLComponent(final String urlComponent, final String encoding) {
		try {
			return URLEncoder.encode(urlComponent, encoding);
		} catch (final UnsupportedEncodingException e) {
			return urlComponent;
		}
	}
	
	/**
	 * Removes all HTML tags from the text. "&lt;b&gt;example&lt;/b&gt;" returns "example".
	 * 
	 * @param html Text with HTML tags.
	 * @return Text without tags.
	 * 
	 * @see Jsoup#clean(String, Whitelist)
	 * @see Whitelist#none()
	 */
	public static String stripHTML(final String html) {
		return html != null ? Jsoup.clean(html, Whitelist.none()) : null;
	}
	
	/**
	 * Remove most unsafe tags and attributes, leaving mostly format tags and links.
	 * 
	 * @param html HTML to be cleaned.
	 * @return Clean HTML.
	 * 
	 * @see Jsoup#clean(String, Whitelist)
	 * @see Whitelist#basic()
	 */
	public static String cleanHTML(final String html) {
		return html != null ? Jsoup.clean(html, Whitelist.basic()) : null;
	}
	
	/**
	 * Une um array separando-as por vírgula ({@link Object#toString()} de cada Objeto)
	 * 
	 * @param collection Coleção de Objetos
	 * @return "objeto, objeto, objeto"
	 */
	public static <T> String join(final T[] collection) {
		return join(Arrays.asList(collection));
	}
	
	/**
	 * Une um array separando-as por vírgula ({@link Object#toString()} de cada Objeto)
	 * 
	 * @param collection Coleção de Objetos
	 * @param separator Separador
	 * 
	 * @return "objeto, objeto, objeto"
	 */
	public static <T> String join(final T[] collection, final String separator) {
		return join(Arrays.asList(collection), separator);
	}
	
	/**
	 * Une uma coleção separando-as por vírgula ({@link Object#toString()} de cada Objeto)
	 * 
	 * @param collection Coleção de Objetos
	 * @return "objeto, objeto, objeto"
	 */
	public static String join(final Collection<?> collection) {
		return join(collection, ", ");
	}
	
	/**
	 * Une uma coleção separando-os pelo separador especificado
	 * 
	 * @param collection Coleção de objetos
	 * @param joinString Separador
	 * @return String composta por "objeto+separador+objeto+separador+objeto"
	 */
	public static String join(final Collection<?> collection, final String joinString) {
		if (collection != null) {
			final StringBuilder str = new StringBuilder();
			
			int i = 0;
			final int max = collection.size();
			for (final Object object : collection) {
				str.append(object.toString());
				
				if (++i < max) {
					str.append(joinString);
				}
			}
			
			return str.toString();
		}
		else {
			return null;
		}
	}
	
	/**
	 * Verifica se uma String existe dentro do conjunto de {@code strings}
	 * @param string A string para verificar
	 * @param strings Lista de strings
	 * @return {@code true} se {@code string} é um dos elementos de {@code strings}
	 */
	public static boolean in(final String string, final String... strings) {
		for (final String s : strings) {
			if (string.equals(s)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Verifica se uma String é vazia ou não. Uma string é considerada vazia se for {@code null} ou igual a {@code ""}.
	 * @param string - A String a ser verificada
	 * @return {@code true} caso a string seja {@code null} ou {@code ""}.
	 */
	public static boolean isEmpty(final String string) {
		return string == null || string.length() == 0;
	}
	
	/**
	 * Verifica se uma String é um número ou não.
	 * @param string - A String a ser verificada
	 * @return {@code true} caso a string seja composta apenas de números.
	 */
	public static boolean isNumber(final String string) {
		return string != null && string.matches("\\d+");
	}
	
	/**
	 * Verifica se uma String é um número hexadecimal ou não.
	 * @param string - A String a ser verificada
	 * @return {@code true} caso a string seja composta apenas de números e letras de A a F.
	 */
	public static boolean isNumberHexa(final String string) {
		return string != null && string.matches("[a-fA-F0-9]+");
	}
	
	/**
	 * Retorna o mapa especificado como parâmetros prontos para envio em URL: {@code param1=valor&param2=valor&param3=valor}
	 * 
	 * @param params Parâmetros para serem serializados
	 * 
	 * @return Parâmetros serializados
	 * 
	 * @see URLEncoder
	 * @see URLEncoder#encode(String, String)
	 */
	public static String asURLParams(final Map<String, ?> params) {
		final StringBuilder str = new StringBuilder();
		
		if (params != null) {
			final Set<?> entrySet = params.entrySet();
			int i = 0;
			final int max = entrySet.size();
			for (final Object name : entrySet) {
				final Entry<String, ?> entry = (Entry<String, ?>) name;
				
				// null
				if (entry.getValue() == null) {
					str.append(entry.getKey());
					str.append("=");
				}
				// O parâmetro é uma Collection?
				else if (Collection.class.isAssignableFrom(entry.getValue().getClass())) {
					final Collection<?> c = (Collection<?>) entry.getValue();
					
					for (final Iterator<?> it = c.iterator(); it.hasNext();) {
						str.append(entry.getKey());
						str.append("=");
						str.append(encodeURLComponent(it.next().toString()));
						
						if (it.hasNext()) {
							str.append("&");
						}
					}
				}
				// O parâmetro é um array?
				else if (entry.getValue().getClass().isArray()) {
					final Object[] array = (Object[]) entry.getValue();
					
					for (final Object object : array) {
						str.append(entry.getKey());
						str.append("=");
						str.append(encodeURLComponent(object.toString()));
					}
				}
				// Parâmetros simples
				else {
					str.append(entry.getKey());
					str.append("=");
					str.append(encodeURLComponent(entry.getValue().toString()));
				}
				
				if (++i < max) {
					str.append("&");
				}
			}
		}
		
		return str.toString();
	}
	
	/**
	 * Verifica se uma String é vazia ou totalmente composta por espaços.
	 * 
	 * @param string - A String a ser verificada
	 * 
	 * @return {@code true} caso a string seja {@link #isEmpty(String) vazia} ou completamente composta de espaços.
	 */
	public static boolean isBlank(final String string) {
		return string == null || string.matches("\\s*");
	}
	
	/**
	 * Lê o conteúdo de um arquivo para uma String. Equivalente a {@code fromStream(new FileInputStream(file))}.
	 * 
	 * @param file - Arquivo
	 * 
	 * @return Conteúdo do arquivo em String
	 * 
	 * @see FileInputStream
	 * @see #fromStream(InputStream)
	 */
	public static String fromFile(final File file) {
		try {
			return fromStream(new FileInputStream(file));
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Lê o stream como uma String.
	 * 
	 * @param stream - Stream
	 * @return Representação em String do Stream
	 */
	public static String fromStream(final InputStream stream) {
		return fromReader(new InputStreamReader(stream));
	}
	
	/**
	 * Lê o Reader como uma String.
	 * 
	 * @param reader - Reader
	 * @return Representação em String do Stream
	 */
	public static String fromReader(final Reader reader) {
		final StringBuilder str = new StringBuilder();
		final BufferedReader br = new BufferedReader(reader);
		String line;
		final String newline = System.getProperty("line.separator");
		
		try {
			while ((line = br.readLine()) != null) {
				str.append(line);
				str.append(newline);
			}
		} catch (final IOException e) {
			e.printStackTrace();
			return null;
		}
		
		return str.toString();
	}
	
	/**
	 * Normaliza uma String para o formato chamado "SEO Name": um-exemplo-de-string-sem-acentos-toda-em-minusculas-e-sem-espacos
	 * 
	 * @param str String a ser normalizada
	 * 
	 * @return string-normalizada-corretamente
	 */
	public static String asSeoName(final String str) {
		if (str != null) {
			return normalize(str).replaceAll("[\\p{Punct}\\s\\-]+", "-").replaceAll("-+$", "").replaceAll("-{2,}", "-").toLowerCase();
		}
		else {
			return null;
		}
	}
	
	/**
	 * <p>Normaliza a String retirando acentos via {@link Normalizer.Form#NFD Decomposição Canônica}.</p>
	 * <p>Primeiro é feita a decomposição da string ("á" se torna "a´") e em seguida
	 * {@link String#replaceAll(String,String) são removidos} todos os caracteres especiais através da seguinte
	 * {@link Matcher Expressão Regular}: {@code (?i)\P{Alnum}|\P{Punct}|\S}</p>
	 * 
	 * @param string String a ser normalizada
	 * 
	 * @return String normalizada
	 */
	public static String normalize(final String string) {
		return string == null ? "" : Normalizer.normalize(string, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
	}
	
	/**
	 * Faz parse de uma representação de valor {@link Boolean booleano}: "{@code 0, false, falso, f, no, não, nao, n}"
	 * representam {@link Boolean#FALSE}. Qualquer outro valor representa {@link Boolean#TRUE}. As comparações não são
	 * case-sensitive.
	 * 
	 * @param string Representação do Booleano
	 * @return {@code true} ou {@code false}
	 */
	public static boolean parseBoolean(final String string) {
		return (string != null) && !string.matches("(?i)0|f(als[eo])?|n([aã]?o)?");
	}
	
	/**
	 * Faz parse de uma representação de valor {@link Boolean booleano}: "{@code 0, false, falso, F, f, no, não, nao, N, n}"
	 * representam {@link Boolean#FALSE}. Qualquer outro valor representa {@link Boolean#TRUE}. As comparações não são
	 * case-sensitive.
	 * 
	 * @param string Representação do Booleano
	 * @param defaultValue Valor padrão caso {@code string} seja {@code null}
	 * @return {@code true} ou {@code false}
	 */
	public static boolean parseBoolean(final String string, final boolean defaultValue) {
		return string == null ? defaultValue : !string.matches("(?i)0|f(als[eo])?|n([aã]?o)?");
	}
	
	/**
	 * Faz parse de uma {@link String} numérica, verificando a String antes de converter e retornando um valor default
	 * caso a String seja nula ou não seja um número. String convertidas sempre serão consideradas como Base 10.
	 * 
	 * @param n String numérica
	 * @param defaultValue Valor default para retornar caso a String seja inválida
	 * @return int relativo a String, ou {@code defaultValue}
	 * 
	 * @see String#matches(String)
	 * @see Integer#parseInt(String, int)
	 */
	public static int parseInteger(final String n, final int defaultValue) {
		if (n != null && n.matches("\\d+")) {
			return Integer.parseInt(n, 10);
		}
		
		return defaultValue;
	}
	
	/**
	 * Faz parse de uma {@link String} numérica, verificando a String antes de converter e retornando um valor default
	 * caso a String seja nula ou não seja um número. String convertidas sempre serão consideradas como Base 10.
	 * 
	 * @param n String numérica
	 * @param defaultValue Valor default para retornar caso a String seja inválida
	 * @return int relativo a String, ou {@code defaultValue}
	 * 
	 * @see String#matches(String)
	 * @see Long#parseLong(String, int)
	 */
	public static long parseLong(final String n, final long defaultValue) {
		if (n != null && n.matches("\\d+")) {
			return Long.parseLong(n, 10);
		}
		
		return defaultValue;
	}
	
	/**
	 * Faz parse de uma {@link String} numérica, verificando a String antes de converter e retornando um valor default
	 * caso a String seja nula ou não seja um número. String convertidas sempre serão consideradas como Base 10.
	 * 
	 * @param n String numérica
	 * @param defaultValue Valor default para retornar caso a String seja inválida
	 * @return int relativo a String, ou {@code defaultValue}
	 * 
	 * @see String#matches(String)
	 * @see Long#parseLong(String, int)
	 */
	public static double parseDouble(final String n, final double defaultValue) {
		if (n != null && n.matches("(\\d+)?\\.?\\d+")) {
			return Double.parseDouble(n);
		}
		
		return defaultValue;
	}
	
	/**
	 * Lê uma Tag HTML/XML e retorna um mapa com os atributos da mesma.
	 * O mapa contém uma chave {@code TAGNAME} que contém o nome da tag processada.
	 * 
	 * @param tag Tag HTML/XML
	 * @return Mapa dos atributos da tag
	 */
	public static Map<String, String> parseHTMLTag(final String tag) {
		if (tag.matches("<[^>]+>")) {
			final Pattern pattern = Pattern.compile("([a-zA-Z0-9\\-]+)=\"([^\"]+)\"");
			final Matcher matcher = pattern.matcher(tag);
			final Map<String, String> params = new HashMap<String, String>();
			params.put("TAGNAME", tag.substring(1, tag.indexOf(' ')));
			
			while (matcher.find()) {
				params.put(matcher.group(1), matcher.group(2));
			}
			
			return params;
		}
		
		return null;
	}
	
	/**
	 * Recebe uma tag e uma {@link #parseHTMLTag(String) lista de parâmetros} e retorna a tag montada
	 * 
	 * @param params Parâmetros da tag
	 * @param shortTag Se a tag será gerada no formato "&lt;tag&gt;" ou "&lt;tag /&gt;"
	 * @return Tag montada
	 * @see #parseHTMLTag(String)
	 */
	public static String buildHTMLTag(final Map<String, String> params, final boolean shortTag) {
		final StringBuilder str = new StringBuilder("<");
		
		str.append(params.get("TAGNAME"));
		str.append(" ");
		
		for (final Entry<String, String> entry : params.entrySet()) {
			if (!entry.getKey().equals("TAGNAME")) {
				str.append(entry.getKey());
				str.append("=\"");
				str.append(entry.getValue());
				str.append("\"");
				str.append(" ");
			}
		}
		
		if (shortTag) {
			str.append("/>");
		}
		else {
			str.deleteCharAt(str.length() - 1);
			str.append(">");
		}
		
		return str.toString();
	}
	
	/**
	 * Creates the MD5 hash for a given string.
	 * 
	 * @param data The string to be hashed
	 * @return The MD5 hash (lowercase)
	 */
	public static String md5(final String data) {
		final StringBuilder hash = new StringBuilder();
		
		try {
			final MessageDigest digester = MessageDigest.getInstance("MD5");
			final byte[] hashedData = digester.digest(data.getBytes());
			for (final byte b : hashedData) {
				hash.append(String.format("%02x", b));
			}
		} catch (final NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return hash.toString();
	}
	
	/**
	 * Escapes SQL-Like wildcard characters on the text. Please, include {@code ESCAPE <escape string>} right after the {@code LIKE}
	 * clause. That is needed for correctly escaping LIKE wildcard characters. It is recommended to use "\" as the escape string:
	 * {@code SELECT ... FROM ... WHERE something LIKE :text ESCAPE '\'}
	 * 
	 * @param text The text
	 * @param escape The {@code ESCAPE} string (recommended: {@code \})
	 * @return The text with the escaped wildcards
	 */
	public static String escapeSQLLike(final String text, final String escape) {
		return text.replaceAll("([%\\[\\]_]+)", escape + "$1");
	}
	
	/**
	 * Downloads data from the especified URL and return it as a String
	 * 
	 * @param url The URL to {@code GET} data from
	 * @return Whatever text the URL returns
	 */
	public static String downloadAsString(final String url) {
		try {
			final URL downloadURL = new URL(url);
			final HttpURLConnection conn = (HttpURLConnection) downloadURL.openConnection();
			conn.setConnectTimeout(1000);
			if (conn.getResponseCode() == 200) {
				final StringBuffer str = new StringBuffer();
				final BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line;
				
				while ((line = br.readLine()) != null) {
					str.append(line);
					str.append(LINE_SEPARATOR);
				}
				
				return str.toString();
			}
			else {
				return null;
			}
		} catch (final Exception e) {
			if (log.isErrorEnabled()) { log.error("Error while downloading " + url, e); }
//			else { e.printStackTrace(); }
			return null;
		}
	}
	
	/**
	 * Fully Capitalizes A String =P
	 * 
	 * @param string The string
	 * @return The Fully Capitalized String
	 */
	public static String capitalizeFully(final String string) {
		final StringBuilder str = new StringBuilder();
		final String[] words = string.split("\\s+");
		
		for (final String word : words) {
			str.append(word.substring(0, 1).toUpperCase());
			str.append(word.substring(1).toLowerCase());
		}
		
		return str.toString();
	}
	
	/**
	 * @param code The markdown code
	 * @return The markdown as HTML
	 */
	public static String markdown(final String code) {
		return new MarkdownProcessor().markdown(code);
	}
	
	/**
	 * @param code The markdown code
	 * @return Clears all Markdown from the text (it processes the markdown then strips ALL HTML)
	 */
	public static String clearMarkdown(final String code) {
		return decodeHTML(Jsoup.clean(markdown(code), Whitelist.none()));
	}
}
