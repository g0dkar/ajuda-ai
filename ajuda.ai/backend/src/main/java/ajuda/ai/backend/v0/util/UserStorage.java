package ajuda.ai.backend.v0.util;
//package ajuda.ai.backend.util;
//
//import java.io.Serializable;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.enterprise.context.RequestScoped;
//import javax.inject.Inject;
//
//import org.infinispan.Cache;
//import org.keycloak.representations.idm.UserRepresentation;
//import org.slf4j.Logger;
//
//import ajuda.ai.util.StringUtils;
//
///**
// * Handles most of the "another User" operations. Mostly getting info about
// * users from their ID (which in turn comes from Keycloak)
// * 
// * @author Rafael Lins
// *
// */
//@RequestScoped
//public class UserStorage implements Serializable {
//	private static final long serialVersionUID = 1L;
//	
//	private final Logger log;
//	private final CacheService cs;
//	private final KeycloakService keycloak;
//	
//	/** @deprecated CDI */ @Deprecated
//	UserStorage() { this(null, null, null, null); }
//	
//	@Inject
//	public UserStorage(final Configuration conf, final CacheService cs, final KeycloakService keycloak, final Logger log) {
//		this.log = log;
//		this.cs = cs;
//		this.keycloak = keycloak;
//		
//		if (conf != null) {
//			keycloak.init(conf);
//		}
//	}
//	
//	public UserRepresentation get(final String id) {
//		final Cache<String, UserRepresentation> cache = cs.getCache("users");
//		UserRepresentation user = cache.get(id);
//		
//		if (user != null) {
//			return user;
//		} else {
//			try {
//				user = keycloak.realm().users().get(id).toRepresentation();
//				
//				cache.put(id, user);
//				
//				if (user.getAttributes() == null) {
//					user.setAttributes(new HashMap<>());
//				}
//				
//				if (!user.getAttributes().containsKey("avatar")) {
//					user.getAttributes().put("avatar", "https://gravatar.com/avatar/" + StringUtils.md5(user.getEmail() != null ? user.getEmail() : user.getId()) + "?d=retro");
//				}
//				
//				return user;
//			} catch (final Exception e) {
//				if (log.isErrorEnabled()) {
//					log.error("Exception while looking up Keycloak User", e);
//				}
//				return null;
//			}
//		}
//	}
//	
//	/**
//	 * Returns a more simplistic user, with only the bare minimal info about it
//	 * in a {@link Map}
//	 * 
//	 * @param id
//	 *            The User ID
//	 * @return {@code id}, {@code type} (always equal to {@code "profile"}),
//	 *         {@code first_name}, {@code last_name}, {@code name} (first name +
//	 *         last, if available, or the username) and {@code avatar}
//	 */
//	public Map<String, Object> getSimple(final String id) {
//		final UserRepresentation user = get(id);
//		if (user != null) {
//			final Map<String, Object> userMap = new HashMap<>(6);
//			userMap.put("id", user.getId());
//			userMap.put("type", "profile");
//			userMap.put("first_name", user.getFirstName());
//			userMap.put("last_name", user.getLastName());
//			userMap.put("name", user.getFirstName() == null ? user.getUsername() : user.getLastName() != null ? user.getFirstName() + " " + user.getLastName() : user.getFirstName());
//			userMap.put("avatar", user.getAttributes().get("avatar"));
//			return userMap;
//		} else {
//			return null;
//		}
//	}
//}
