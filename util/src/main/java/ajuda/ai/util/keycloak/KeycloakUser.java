package ajuda.ai.util.keycloak;

import java.io.Serializable;
import java.security.Principal;
import java.util.Set;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessToken.Access;

/**
 * Facade to easily access data about the currently logged in user (which comes from Keycloak)
 * @author g0dkar
 *
 */
@RequestScoped
public class KeycloakUser implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final String id;
	private final AccessToken accessToken;
	
	/** @deprecated CDI */
	@Deprecated KeycloakUser() { this(null); }
	
	@Inject
	public KeycloakUser(final HttpServletRequest request) {
		if (request != null) {
			final Principal principal = request.getUserPrincipal();
			if (principal != null) {
				if (request.getUserPrincipal() instanceof KeycloakPrincipal) {
					final KeycloakPrincipal<KeycloakSecurityContext> keycloakPrincipal = (KeycloakPrincipal) principal;
					id = keycloakPrincipal.getName();
					accessToken = keycloakPrincipal.getKeycloakSecurityContext().getToken();
				}
				else {
					id = principal.getName();
					accessToken = null;
				}
			}
			else {
				id = null;
				accessToken = null;
			}
		}
		else {
			id = null;
			accessToken = null;
		}
	}

	public String getId() {
		return id;
	}

	public AccessToken getAccessToken() {
		return accessToken;
	}
	
	public String getName() {
		return accessToken.getName();
	}

	public String getGivenName() {
		return accessToken.getGivenName();
	}

	public String getFamilyName() {
		return accessToken.getFamilyName();
	}

	public String getMiddleName() {
		return accessToken.getMiddleName();
	}

	public String getNickName() {
		return accessToken.getNickName();
	}

	public String getPreferredUsername() {
		return accessToken.getPreferredUsername();
	}

	public String getProfile() {
		return accessToken.getProfile();
	}

	public String getPicture() {
		return accessToken.getPicture();
	}

	public String getWebsite() {
		return accessToken.getWebsite();
	}

	public String getEmail() {
		return accessToken.getEmail();
	}

	public Boolean getEmailVerified() {
		return accessToken.getEmailVerified();
	}

	public String getGender() {
		return accessToken.getGender();
	}

	public String getBirthdate() {
		return accessToken.getBirthdate();
	}

	public String getZoneinfo() {
		return accessToken.getZoneinfo();
	}

	public String getLocale() {
		return accessToken.getLocale();
	}

	public String getPhoneNumber() {
		return accessToken.getPhoneNumber();
	}

	public Boolean getPhoneNumberVerified() {
		return accessToken.getPhoneNumberVerified();
	}
	
	public Access getAccess() {
		return accessToken.getRealmAccess();
	}
	
	public Set<String> getRoles() {
		return getAccess().getRoles();
	}
	
	public boolean isRole(final String role) {
		return getRoles().contains(role);
	}
}
