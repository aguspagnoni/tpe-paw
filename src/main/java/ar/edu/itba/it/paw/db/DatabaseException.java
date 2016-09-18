package ar.edu.itba.it.paw.db;

/**
 * Unchecked exception thrown on fatal database error
 */
public class DatabaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DatabaseException(String message, Throwable cause) {
		super(message, cause);
	}
}
