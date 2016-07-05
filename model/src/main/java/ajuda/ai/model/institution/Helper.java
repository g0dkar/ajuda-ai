package ajuda.ai.model.institution;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Representa uma pessoa que está ajudando a {@link Institution Instituições}
 * 
 * @author Rafael Lins - g0dkar
 *
 */
@Entity
public class Helper implements Serializable {
	/** Serial Version UID */
	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToMany(fetch = FetchType.LAZY)
	private Set<Institution> institutions;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false)
	private Date timestamp;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastLoginTimestamp;
	
	@Size(max = 128)
	@Column(length = 128)
	private String name;
	
	@Email
	@NotBlank
	@Size(max = 128)
	@Column(nullable = false, unique = true, length = 128)
	private String email;
	
	@Column(nullable = false)
	private boolean anonymous;
	
	@Column(length = 60, name = "pass")
	private String password;
	
	@Column(nullable = false)
	private int score;
	
	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(final Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public boolean isAnonymous() {
		return anonymous;
	}

	public void setAnonymous(final boolean anonymous) {
		this.anonymous = anonymous;
	}

	public Set<Institution> getInstitutions() {
		return institutions;
	}

	public void setInstitutions(final Set<Institution> institutions) {
		this.institutions = institutions;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public int getScore() {
		return score;
	}

	public void setScore(final int score) {
		this.score = score;
	}
	
	public void incScore() {
		score++;
	}

	public Date getLastLoginTimestamp() {
		return lastLoginTimestamp;
	}

	public void setLastLoginTimestamp(final Date lastLoginTimestamp) {
		this.lastLoginTimestamp = lastLoginTimestamp;
	}
}
