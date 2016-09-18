package ar.edu.itba.it.paw.exception;

import ar.edu.itba.it.paw.domain.user.User;

public class DuplicatedUserException extends RuntimeException {

	private static final long serialVersionUID = 2797357501840226962L;
	private User user;

	public DuplicatedUserException(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}
}
