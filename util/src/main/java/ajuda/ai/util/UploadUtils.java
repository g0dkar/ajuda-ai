package ajuda.ai.util;

import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class with utilities for working with Uploads
 * @author Rafael g0dkar
 *
 */
public class UploadUtils {
	private static final Logger LOG = LoggerFactory.getLogger(UploadUtils.class);
	
	/**
	 * Checks if the {@link InputStream} data is a valid image in one of {@code expectedExtensions} types
	 * @param input The file data
	 * @return {@code true} if it's an valid image in one for the informed extensions
	 */
	public static boolean isValidImage(final InputStream input) {
		if (LOG.isDebugEnabled()) { LOG.debug("Validating image from InputStream"); }
		
		if (input != null) {
			try {
				ImageIO.read(input);
			} catch (final IOException e) {
				if (LOG.isDebugEnabled()) { LOG.debug("Error while reading image", e); }
			}
			
			return true;
		}
		else if (LOG.isDebugEnabled()) { LOG.debug("InputStream is null"); }
		
		return false;
	}
}
