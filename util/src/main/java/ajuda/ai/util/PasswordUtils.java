package ajuda.ai.util;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for generating and validating Secure Passwords.
 * 
 * Found it somewhere on the Internet then I built my code around it.
 * 
 */
public class PasswordUtils {
	public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";
	
	// The following constants may be changed without breaking existing hashes.
	public static final int PBKDF2_ITERATIONS = 10000;
	public static final int SALT_BYTE_SIZE = 24;
	public static final int HASH_BYTE_SIZE = 24;
	
	public static final int SALT_INDEX = 0;
	public static final int SALT_LENGTH = 48;
	public static final int PBKDF2_INDEX = 48;
	public static final int PBKDF2_LENGTH = 48;
	
	public static final boolean BCRYPT_SUPPORT;
	
	static {
		Class<?> klass = null;
		
		try {
			klass = Class.forName("org.mindrot.jbcrypt.BCrypt");
		} catch (final ClassNotFoundException e) {
			klass = null;
		}
		
		BCRYPT_SUPPORT = klass != null;
	}
	
	/**
	 * Creates a password using {@link BCrypt}.
	 * 
	 * @param password The password
	 * 
	 * @return The hash
	 */
	public static String createBCryptHash(final String password) {
		if (BCRYPT_SUPPORT) {
			return BCrypt.hashpw(password, BCrypt.gensalt(SALT_LENGTH));
		}
		else {
			throw new UnsupportedOperationException("BCrypt isn't available on the Classpath (class org.mindrot.jbcrypt.BCrypt wasn't found)");
		}
	}
	
	/**
	 * Checks a {@link BCrypt}-generated hash against a password.
	 * @param password The password
	 * @param hash The hash
	 * @return {@code true} if the password is right, {@code false} otherwise.
	 */
	public static boolean validateBCryptHash(final String password, final String hash) {
		return BCrypt.checkpw(password, hash);
	}
	
	/**
	 * Returns a salted PBKDF2 hash of the password.
	 * 
	 * @param password
	 *            the password to hash
	 * @return a salted PBKDF2 hash of the password
	 */
	public static String createHash(final String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		return createHash(password.toCharArray());
	}
	
	/**
	 * Returns a salted PBKDF2 hash of the password.
	 * 
	 * @param password
	 *            the password to hash
	 * @return a salted PBKDF2 hash of the password
	 */
	public static String createHashSafe(final String password) {
		try {
			return createHash(password.toCharArray());
		} catch (final NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (final InvalidKeySpecException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Returns a salted PBKDF2 hash of the password.
	 * 
	 * @param password
	 *            the password to hash
	 * @return a salted PBKDF2 hash of the password
	 */
	public static String createHash(final char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		// Generate a random salt
		final SecureRandom random = new SecureRandom();
		final byte[] salt = new byte[SALT_BYTE_SIZE];
		random.nextBytes(salt);
		
		// Hash the password
		final byte[] hash = pbkdf2(password, salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE);
		// format salt + hash
		return toHex(salt) + toHex(hash);
	}
	
	/**
	 * Returns a salted PBKDF2 hash of the password.
	 * 
	 * @param password
	 *            the password to hash
	 * @return a salted PBKDF2 hash of the password
	 */
	public static String createHash(final String password, final int saltByteSize, final int hashByteSize) {
		try {
			// Generate a random salt
			final SecureRandom random = new SecureRandom();
			final byte[] salt = new byte[saltByteSize];
			random.nextBytes(salt);
			
			// Hash the password
			final byte[] hash = pbkdf2(password.toCharArray(), salt, PBKDF2_ITERATIONS, hashByteSize);
			// format salt + hash
			return toHex(salt) + toHex(hash);
		} catch (final Exception e) {
			return null;
		}
	}
	
	/**
	 * Validates a password using a hash.
	 * 
	 * @param password
	 *            the password to check
	 * @param correctHash
	 *            the hash of the valid password
	 * @return true if the password is correct, false if not
	 */
	public static boolean validatePassword(final String password, final String correctHash) throws NoSuchAlgorithmException, InvalidKeySpecException {
		return validatePassword(password.toCharArray(), correctHash);
	}
	
	public static boolean validatePasswordSafe(final String password, final String correctHash) {
		try {
			return password != null && validatePassword(password, correctHash);
		} catch (final NoSuchAlgorithmException e) {
			System.out.println("[Password.validatePasswordSafe()] NoSuchAlgorithmException while validating:");
			e.printStackTrace(System.out);
			
			return false;
		} catch (final InvalidKeySpecException e) {
			System.out.println("[Password.validatePasswordSafe()] InvalidKeySpecException while validating:");
			e.printStackTrace(System.out);
			
			return false;
		} catch (final Throwable t) {
			// This is to make sure NOTHING BUT A VALID PASSWORD passes as valid.
			System.out.println("[Password.validatePasswordSafe()] Throwable while validating:");
			t.printStackTrace(System.out);
			
			return false;
		}
	}
	
	/**
	 * Validates a password using a hash.
	 * 
	 * @param password
	 *            the password to check
	 * @param correctHash
	 *            the hash of the valid password
	 * @return true if the password is correct, false if not
	 */
	public static boolean validatePassword(final char[] password, final String correctHash) throws NoSuchAlgorithmException, InvalidKeySpecException {
		// Decode the hash into its parameters
		final byte[] salt = fromHex(correctHash.substring(SALT_INDEX, SALT_INDEX + SALT_LENGTH));
		final byte[] hash = fromHex(correctHash.substring(PBKDF2_INDEX, PBKDF2_INDEX + PBKDF2_LENGTH));
		// Compute the hash of the provided password, using the same salt,
		// iteration count, and hash length
		final byte[] testHash = pbkdf2(password, salt, PBKDF2_ITERATIONS, hash.length);
		// Compare the hashes in constant time. The password is correct if
		// both hashes match.
		return slowEquals(hash, testHash);
	}
	
	/**
	 * Compares two byte arrays in length-constant time. This comparison method is used so that password hashes cannot
	 * be extracted from an on-line system using a timing attack and then attacked off-line.
	 * 
	 * @param a
	 *            the first byte array
	 * @param b
	 *            the second byte array
	 * @return true if both byte arrays are the same, false if not
	 */
	private static boolean slowEquals(final byte[] a, final byte[] b) {
		int diff = a.length ^ b.length;
		for (int i = 0; i < a.length && i < b.length; i++) {
			diff |= a[i] ^ b[i];
		}
		return diff == 0;
	}
	
	/**
	 * Computes the PBKDF2 hash of a password.
	 * 
	 * @param password
	 *            the password to hash.
	 * @param salt
	 *            the salt
	 * @param iterations
	 *            the iteration count (slowness factor)
	 * @param bytes
	 *            the length of the hash to compute in bytes
	 * @return the PBDKF2 hash of the password
	 */
	private static byte[] pbkdf2(final char[] password, final byte[] salt, final int iterations, final int bytes) throws NoSuchAlgorithmException, InvalidKeySpecException {
		final PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
		final SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
		return skf.generateSecret(spec).getEncoded();
	}
	
	/**
	 * Converts a string of hexadecimal characters into a byte array.
	 * 
	 * @param hex
	 *            the hex string
	 * @return the hex string decoded into a byte array
	 */
	private static byte[] fromHex(final String hex) {
		final byte[] binary = new byte[hex.length() / 2];
		for (int i = 0; i < binary.length; i++) {
			binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return binary;
	}
	
	/**
	 * Converts a byte array into a hexadecimal string.
	 * 
	 * @param array
	 *            the byte array to convert
	 * @return a length*2 character string encoding the byte array
	 */
	private static String toHex(final byte[] array) {
		final BigInteger bi = new BigInteger(1, array);
		final String hex = bi.toString(16);
		final int paddingLength = (array.length * 2) - hex.length();
		if (paddingLength > 0) {
			return String.format("%0" + paddingLength + "d", 0) + hex;
		} else {
			return hex;
		}
	}
	
	/**
	 * Possible scores of a {@link PasswordUtils#scoreStrength(String) password strength test}.
	 */
	public static enum PasswordStrength {
		/** Nope: Not in a million years. Probably only numbers. */
		NOPE,
		/** Bad: Please, avoid this. Probably "abc123" */
		BAD,
		/** Meh: Not even good, but acceptable */
		MEH,
		/** Good: Starting to look better. Foolproof at least... I hope. */
		GOOD,
		/** Very Good: Not bad at all! */
		VERY_GOOD,
		/** Epic: Unbreakable, uncrackable. */
		EPIC;
	}
	
	private static final Pattern PATTERN_PWD_NUMBERS = Pattern.compile("[0-9]");
	private static final Pattern PATTERN_PWD_LOWER = Pattern.compile("[a-z]");
	private static final Pattern PATTERN_PWD_UPPER = Pattern.compile("[A-Z]");
	private static final Pattern PATTERN_PWD_PUNCT = Pattern.compile("\\p{Punct}");
	private static final Pattern PATTERN_PWD_SPACE = Pattern.compile("\\s");
	
	/**
	 * <p>Scores the password strength {@link Matcher#find() according} to:</p>
	 * 
	 * <ol>
	 * <li>Have Numbers? {@link #PATTERN_PWD_NUMBERS}</li>
	 * <li>Have Lowercase Characters? {@link #PATTERN_PWD_LOWER}</li>
	 * <li>Have Uppercase Characters? {@link #PATTERN_PWD_UPPER}</li>
	 * <li>Have Punctuation? {@link #PATTERN_PWD_PUNCT}</li>
	 * <li>Have Spaces? {@link #PATTERN_PWD_SPACE}</li>
	 * <li>Is longer than 20 characters? {@link String#length() length}{@code &nbsp;&gt; 20}</li>
	 * </ol>
	 * 
	 * <p>Each criteria that passes awards a "point". The final value is a {@link PasswordStrength} enum constant indicating it's score.</p>
	 * 
	 * @param password The password
	 * @return The Score
	 * @see PasswordStrength
	 */
	public static PasswordStrength scoreStrength(final String password) {
		int value = -1;
		
		if (!StringUtils.isBlank(password)) {
			if (PATTERN_PWD_NUMBERS.matcher(password).find()) {
				value++;
			}
			
			if (PATTERN_PWD_LOWER.matcher(password).find()) {
				value++;
			}
			
			if (PATTERN_PWD_UPPER.matcher(password).find()) {
				value++;
			}
			
			if (PATTERN_PWD_PUNCT.matcher(password).find()) {
				value++;
			}
			
			if (PATTERN_PWD_SPACE.matcher(password).find()) {
				value++;
			}
			
			if (password.length() > 20) {
				value++;
			}
		}
		
		return value > 0 ? (value < PasswordStrength.values().length ? PasswordStrength.values()[value] : PasswordStrength.EPIC) : PasswordStrength.NOPE;
	}
}