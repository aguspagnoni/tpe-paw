package ar.edu.itba.it.paw.exception;

public class InvalidPasswordException extends RuntimeException {

	private static final long serialVersionUID = 1852912832230321020L;
	private String password;

	public InvalidPasswordException(String password) {
		this.password = password;
	}

	public String getUsername() {
		return password;
	}
}
