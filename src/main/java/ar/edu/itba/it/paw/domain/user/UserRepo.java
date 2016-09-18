package ar.edu.itba.it.paw.domain.user;

import java.util.List;

public interface UserRepo {

	/**
	 * Gets all users.
	 * */

	public List<User> getAll();

	/**
	 * Gets user matching with id.
	 * */

	public User get(int id);

	/**
	 * Adds a new user.
	 * */

	public void add(User user);

	/**
	 * Authenticates an user. If credentials are valid, it returns the
	 * associated {@code User}. Returns null otherwise.
	 */

	public User authenticate(String username, String password);

	public User authenticateByToken(String username, String token);

	/**
	 * Gets users whose usernames match username.
	 */

	public Iterable<User> getAllMatchingUsers(String username);

	/**
	 * Gets user matching with username.
	 * */

	public User getByName(String username);

	/**
	 * Gets all users matching username and sorts them alphabetically.
	 * */

	public Iterable<User> getMatchingSorted(String username);

	/**
	 * Gets all users and sorts them alphabetically.
	 * */

	public Iterable<User> getAllSorted();

	/**
	 * Gets the users that follow user.
	 * */

	public Iterable<User> getFollowers(String username);
}
