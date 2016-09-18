package ar.edu.itba.it.paw.domain.user;

import java.util.Collections;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.it.paw.domain.common.AbstractHibernateRepo;
import ar.edu.itba.it.paw.exception.DuplicatedUserException;

@Repository
public class HibernateUserRepo extends AbstractHibernateRepo implements
		UserRepo {

	@Autowired
	public HibernateUserRepo(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public void add(User user) {
		if (existsUsername(user.getUsername())) {
			throw new DuplicatedUserException(user);
		}
		save(user);
	}

	@Override
	public User get(int id) {
		return get(User.class, id);
	}

	@Override
	public List<User> getAll() {
		return find("FROM User");
	}

	private boolean existsUsername(String username) {
		return !find("FROM User WHERE username = ?", username).isEmpty();
	}

	@Override
	public User authenticate(String username, String password) {
		List<User> result = find(
				"FROM User WHERE username = ? AND password = ?", username,
				password);
		return result.size() > 0 ? result.get(0) : null;
	}

	@Override
	public User authenticateByToken(String username, String token) {
		List<User> result = find("FROM User WHERE username = ? AND token = ?",
				username, token);
		return result.size() > 0 ? result.get(0) : null;
	}

	@Override
	public Iterable<User> getAllMatchingUsers(String username) {
		if (username == null) {
			return getAll();
		}
		String lowercase = username.toLowerCase();
		return find(
				"FROM User WHERE lower(username) LIKE ? OR lower(firstname) LIKE ? OR lower(lastname) LIKE ?",
				"%" + lowercase + "%", "%" + lowercase + "%", "%" + lowercase
						+ "%");
	}

	@Override
	public User getByName(String username) {
		List<User> result = find("FROM User WHERE username = ?", username);
		return result.size() > 0 ? result.get(0) : null;
	}

	@Override
	public Iterable<User> getMatchingSorted(String username) {
		Iterable<User> users = getAllMatchingUsers(username);
		if (users != null) {
			Collections.sort((List<User>) users);
		}
		return users;
	}

	@Override
	public Iterable<User> getAllSorted() {
		Iterable<User> users = getAll();
		if (users != null) {
			Collections.sort((List<User>) users);
		}
		return users;
	}

	@Override
	public Iterable<User> getFollowers(String username) {
		List<User> result = find(
				"SELECT user FROM User user JOIN user.followees followees WHERE followees.username = ?",
				username);
		return result.size() > 0 ? result : null;
	}
}
