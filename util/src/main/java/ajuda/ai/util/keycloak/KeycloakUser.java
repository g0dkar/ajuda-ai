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
		return accessToken == null ? null : accessToken.getName();
	}

	public String getGivenName() {
		return accessToken == null ? null : accessToken.getGivenName();
	}

	public String getFamilyName() {
		return accessToken == null ? null : accessToken.getFamilyName();
	}

	public String getMiddleName() {
		return accessToken == null ? null : accessToken.getMiddleName();
	}

	public String getNickName() {
		return accessToken == null ? null : accessToken.getNickName();
	}

	public String getPreferredUsername() {
		return accessToken == null ? null : accessToken.getPreferredUsername();
	}

	public String getProfile() {
		return accessToken == null ? null : accessToken.getProfile();
	}

	public String getPicture() {
		return accessToken == null ? null : accessToken.getPicture();
	}

	public String getWebsite() {
		return accessToken == null ? null : accessToken.getWebsite();
	}

	public String getEmail() {
		return accessToken == null ? null : accessToken.getEmail();
	}

	public Boolean getEmailVerified() {
		return accessToken == null ? null : accessToken.getEmailVerified();
	}

	public String getGender() {
		return accessToken == null ? null : accessToken.getGender();
	}

	public String getBirthdate() {
		return accessToken == null ? null : accessToken.getBirthdate();
	}

	public String getZoneinfo() {
		return accessToken == null ? null : accessToken.getZoneinfo();
	}

	public String getLocale() {
		return accessToken == null ? null : accessToken.getLocale();
	}

	public String getPhoneNumber() {
		return accessToken == null ? null : accessToken.getPhoneNumber();
	}

	public Boolean getPhoneNumberVerified() {
		return accessToken == null ? null : accessToken.getPhoneNumberVerified();
	}
	
	public Access getAccess() {
		return accessToken == null ? null : accessToken.getRealmAccess();
	}
	
	public Set<String> getRoles() {
		return accessToken == null ? null : getAccess().getRoles();
	}
	
	public boolean isRole(final String role) {
		return accessToken == null ? false : getRoles().contains(role);
	}
}
