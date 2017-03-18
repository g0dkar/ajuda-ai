package ajuda.ai.model.user;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import br.com.caelum.vraptor.serialization.SkipSerialization;

/**
 * Representa um usuário do sistema
 * 
 * @author Rafael Lins
 *
 */
@Entity
public class User implements Serializable {
	private static final long serialVersionUID = -8760246744870950716L;

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Size(max = 64)
	@Column(nullable = false, unique = true, length = 64)
	@Pattern(regexp = "[a-zA-Z0-9.\\-]+")
	private String username;
	
	@NotBlank
	@SkipSerialization
	@Size(min = 60, max = 60)
	@Column(nullable = false, length = 60)
	private String password;
	
	@Transient
	private String newPassword;
	
	@Email
	@NotBlank
	@Size(max = 128)
	@Column(nullable = false, unique = true, length = 128)
	private String email;
	
	@NotBlank
	@Size(max = 64)
	@Column(nullable = false, length = 64)
	private String firstname;
	
	@NotBlank
	@Size(max = 128)
	@Column(nullable = false, length = 128)
	private String lastname;
	
	@SkipSerialization
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date creationTime;
	
	@SkipSerialization
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLogin;
	
	@SkipSerialization
	@Column(length = 42)
	private String lastLoginIp;
	
	@Column(nullable = false)
	private boolean isInstitution;
	
	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(final String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(final String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(final String lastname) {
		this.lastname = lastname;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(final Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(final Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public String getLastLoginIp() {
		return lastLoginIp;
	}

	public void setLastLoginIp(final String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(final String newPassword) {
		this.newPassword = newPassword;
	}

	public boolean isInstitution() {
		return isInstitution;
	}

	public void setInstitution(final boolean isInstitution) {
		this.isInstitution = isInstitution;
	}
}
