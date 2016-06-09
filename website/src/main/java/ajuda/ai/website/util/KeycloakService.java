package ajuda.ai.website.util;

import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RealmsResource;
import org.keycloak.admin.client.resource.ServerInfoResource;
import org.keycloak.admin.client.token.TokenManager;

/**
 * <p>An {@link ApplicationScoped} holder of a {@link Keycloak} instance so we
 * don't need to create, login, exchange tokens and all that every time we need
 * something from Keycloak. User must
 * {@link #init(String, String, String, String, String, String) initialize} it
 * before using.
 * 
 * <hr>
 * 
 * <p>How to make this work:
 * 
 * <ul>
 * <li>Create a Realm
 * <li>Create a Client just for this (the Client must be of Access Type = Confidential)
 * <li>Create a User just for this (make sure it doesn't have any Required User Actions)
 * <li>Add the {@code realm-admin} role from the {@code realm-management} client
 * </ul>
 * 
 * <p>This user will be able to do anything the Keycloak Admin Client offers.
 * 
 * @author Rafael Lins
 * 
 * @see #init(Configuration)
 * @see #init(String, String, String, String, String, String)
 */

@Singleton
public class KeycloakService {
	private Keycloak keycloak = null;
	private String realm;
	
	/**
	 * Initializes everything loading parameters from the database using the
	 * specified {@link Configuration} instance. Does nothing if already
	 * initialized.
	 * 
	 * @param config
	 *            The Configuration instance
	 * @return {@code this} instance
	 */
	public KeycloakService init(final Configuration config) {
		if (keycloak == null) {
			final String realm = config.get("keycloak.realm", "ajuda-ai");
			final String host = config.get("keycloak.host", "http://localhost/auth");
			final String username = config.get("keycloak.management.username", "ajuda-ai.backend");
			final String password = config.get("keycloak.management.password", "password");
			final String clientId = config.get("keycloak.management.clientId", "ajuda-ai-backend");
			final String clientSecret = config.get("keycloak.management.clientSecret", "secret");
			
			return init(host, realm, username, password, clientId, clientSecret);
		}
		else {
			return this;
		}
	}
	
	/**
	 * Initializes everything using these parameters. Does nothing if already
	 * initialized.
	 * 
	 * @param host
	 *            The Keycloak host (for example {@code http://localhost/auth})
	 * @param realm
	 *            The Realm to login into (the user and client must be from this
	 *            realm)
	 * @param username
	 *            Username
	 * @param password
	 *            Password
	 * @param clientId
	 *            Client against which we'll login
	 * @param clientSecret
	 *            Client secret to login
	 * @return {@code this} instance
	 */
	public KeycloakService init(final String host, final String realm, final String username, final String password, final String clientId, final String clientSecret) {
		if (keycloak == null) {
			this.realm = realm;
			keycloak = Keycloak.getInstance(host, realm, username, password, clientId, clientSecret);
		}
		
		return this;
	}
	
	public Keycloak keycloak() {
		return keycloak;
	}
	
	public RealmsResource realms() {
		return keycloak.realms();
	}
	
	public RealmResource realm(final String realmName) {
		return keycloak.realm(realmName);
	}
	
	public RealmResource realm() {
		return keycloak.realm(realm);
	}
	
	public ServerInfoResource serverInfo() {
		return keycloak.serverInfo();
	}
	
	public TokenManager tokenManager() {
		return keycloak.tokenManager();
	}
	
	@PreDestroy
	public void finalize() {
		if (keycloak != null) {
			keycloak.close();
			keycloak = null;
		}
	}
}
