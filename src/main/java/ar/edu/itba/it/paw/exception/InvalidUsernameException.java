package ar.edu.itba.it.paw.exception;

public class InvalidUsernameException extends RuntimeException {

	private static final long serialVersionUID = 1438745589988083492L;
	private String username;

	public InvalidUsernameException(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}
}
