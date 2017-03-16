package ajuda.ai.backend.v1.auth;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;

import ajuda.ai.model.user.User;

/**
 * Mantém, na {@link SessionScoped sessão}, que usuário está logado, se houve algum.
 * @author Rafael Lins
 *
 */
@SessionScoped
public class AuthenticatedUser implements Serializable {
	private static final long serialVersionUID = -7033448814743585706L;
	
	private User user;
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public boolean isLoggedIn() {
		return user != null;
	}
	
	public User get() {
		return user;
	}

	public Long getId() {
		return user.getId();
	}

	public String getUsername() {
		return user.getUsername();
	}

	public String getPassword() {
		return user.getPassword();
	}

	public String getEmail() {
		return user.getEmail();
	}

	public String getFirstname() {
		return user.getFirstname();
	}

	public String getLastname() {
		return user.getLastname();
	}
}
