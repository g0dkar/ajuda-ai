package ajuda.ai.model.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Maps a Keycloak User. Makes it easier to work with its users and Hibernate can make Foreign Keys.
 * 
 * Not sure if this could be done in other way. Probably yes.
 * 
 * @author Rafael Lins - g0dkar
 *
 */
@Entity(name = "User")
@Table(name = "user_entity", schema = "ajudaai_users")
public class KeycloakDBUser implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID", length = 36)
	private String id;
	
	@Column(name = "EMAIL")
	private String email;
	
	@Column(name = "EMAIL_VERIFIED")
	private boolean emailVerified;
	
	@Column(name = "ENABLED")
	private boolean enabled;
	
	@Column(name = "FIRST_NAME")
	private String firstName;
	
	@Column(name = "LAST_NAME")
	private String lastName;
	
	@Column(name = "USERNAME")
	private String username;
	
	@Column(name = "CREATED_TIMESTAMP")
	private Long created;

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public boolean isEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(final boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public Long getCreated() {
		return created;
	}

	public void setCreated(final Long created) {
		this.created = created;
	}
}
