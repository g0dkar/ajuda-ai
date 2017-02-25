package ajuda.ai.model.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.google.gson.annotations.Expose;

/**
 * Representa um usuário
 * @author Rafael Lins
 *
 */
@Entity
public class User implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Expose
	@NotBlank
	@Size(max = 64)
	@Column(nullable = false, unique = true, length = 64)
	private String username;
	
	@NotBlank
	@Size(min = 60, max = 60)
	@Column(nullable = false, length = 60)
	private String password;
	
	@Expose
	@Email
	@NotBlank
	@Size(max = 128)
	@Column(nullable = false, unique = true, length = 128)
	private String email;
	
	@Expose
	@NotBlank
	@Size(max = 64)
	@Column(nullable = false, length = 64)
	private String firstname;
	
	@Expose
	@NotBlank
	@Size(max = 128)
	@Column(nullable = false, length = 128)
	private String lastname;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
}
