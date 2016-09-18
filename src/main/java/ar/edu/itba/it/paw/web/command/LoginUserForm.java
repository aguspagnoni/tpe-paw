package ar.edu.itba.it.paw.web.command;

import ar.edu.itba.it.paw.domain.user.User;

public class LoginUserForm {
	private User user;

	private String username;
	private String password;

	public LoginUserForm() {
	}

	public LoginUserForm(User user) {
		this.user = user;
		username = user.getUsername();
		password = user.getPassword();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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
}
