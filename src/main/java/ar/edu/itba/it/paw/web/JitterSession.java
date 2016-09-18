package ar.edu.itba.it.paw.web;

import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepo;

public class JitterSession extends WebSession {
	private static final long serialVersionUID = 527029568047508388L;
	private String username;
	private Integer userId;

	public static JitterSession get() {
		return (JitterSession) Session.get();
	}

	public JitterSession(Request request) {
		super(request);
	}

	public String getUsername() {
		return username;
	}

	// getId is reserved, thus getUserId
	public Integer getUserId() {
		return userId;
	}

	public boolean logIn(String username, String password, UserRepo users) {
		User user = users.authenticate(username, password);
		if (user != null) {
			this.username = username;
			this.userId = user.getId();
			return true;
		}
		return false;
	}

	public void logInByToken(String username, String token, UserRepo users) {
		User user = users.authenticateByToken(username, token);
		if (user != null) {
			this.username = username;
			this.userId = user.getId();
		}
	}

	public boolean isLoggedIn() {
		return username != null;
	}

	public void logOut() {
		invalidate();
		clear();
	}
}
