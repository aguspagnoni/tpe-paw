package ar.edu.itba.it.paw.domain.user;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.edu.itba.it.paw.domain.common.PersistentEntity;
import ar.edu.itba.it.paw.domain.gathering.Gathering;
import ar.edu.itba.it.paw.domain.notification.Notification;
import ar.edu.itba.it.paw.domain.notification.Notification.Type;
import ar.edu.itba.it.paw.domain.post.Post;
import ar.edu.itba.it.paw.exception.InvalidPasswordException;
import ar.edu.itba.it.paw.exception.InvalidUsernameException;
import ar.edu.itba.it.paw.exception.NotOwnerException;
import ar.edu.itba.it.paw.web.JitterApplication;

@Entity
@Table(name = "users")
public class User extends PersistentEntity implements Comparable<User> {

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@Column(unique = true, updatable = false, nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;

	private String description;

	@Column(nullable = false)
	private String answer;

	@Column(nullable = false)
	private String question;

	@Column(nullable = false)
	private Timestamp created;

	@Column
	private String token;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Gathering> gatherings = new ArrayList<Gathering>();

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Post> posts = new ArrayList<Post>();

	// sprint2
	@Column(nullable = false)
	private int profileVisits;

	@Column(nullable = false)
	private int followers;

	@Column(nullable = false)
	private Boolean viewable;

	@ManyToMany(cascade = CascadeType.ALL)
	private List<User> followees = new ArrayList<User>();

	@ManyToMany
	@JoinTable(name = "favorite_posts", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "post_id"))
	private List<Post> favoritePosts = new ArrayList<Post>();

	@OneToMany(cascade = CascadeType.ALL)
	private List<Notification> notifications = new ArrayList<Notification>();

	// ManyToMany?
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "users_blacklist")
	private List<User> blacklist = new ArrayList<User>();

	User() {
	}

	public User(String username, String password, String firstName,
			String lastName, String question, String answer,
			String description, int profileVisits, int followers,
			Boolean viewable) {
		if (username == null || password == null || firstName == null
				|| lastName == null || question == null || answer == null
				|| description == null || viewable == null) {
			throw new IllegalArgumentException();
		}
		if (!username.matches("^\\w+")) {
			throw new InvalidUsernameException(username);
		}
		this.username = username;
		if (!password.matches("^\\w+{6,}")) {
			throw new InvalidPasswordException(password);
		}
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.question = question;
		this.answer = answer;
		this.description = description;
		this.created = new Timestamp(java.util.Calendar.getInstance()
				.getTimeInMillis());
		this.profileVisits = 0;
		this.followers = 0;
		this.viewable = viewable;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getConfirmPassword() {
		return password;
	}

	public void setConfirmPassword(String password) {
		this.password = password;
	}

	public void setPassword(String password) {
		if (password == null || password.length() < 6) {
			throw new IllegalArgumentException();
		}
		this.password = password;
	}

	public Timestamp getCreated() {
		return created;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		if (firstName == null || firstName.length() == 0) {
			throw new IllegalArgumentException();
		}
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		if (lastName == null || lastName.length() == 0) {
			throw new IllegalArgumentException();
		}
		this.lastName = lastName;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		if (answer == null || answer.length() == 0) {
			throw new IllegalArgumentException();
		}
		this.answer = answer;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		if (question == null || question.length() == 0) {
			throw new IllegalArgumentException();
		}
		this.question = question;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		if (description == null) {
			throw new IllegalArgumentException();
		}
		this.description = description;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public Boolean getViewable() {
		return this.viewable;
	}

	public void setViewable(Boolean viewable) {
		this.viewable = viewable;
	}

	public int getProfileVisits() {
		return profileVisits;
	}

	public void increaseVisits() {
		this.profileVisits++;
	}

	public int getFollowers() {
		return followers;
	}

	private void increaseFollowers() {
		this.followers++;
	}

	private void decreaseFollowers() {
		this.followers--;
	}

	public List<User> getFollowees() {
		return followees;
	}

	public List<Post> getFavoritePosts() {
		return favoritePosts;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void clearToken() {
		this.token = null;
	}

	public void post(Post post) {
		if (post == null) {
			throw new IllegalArgumentException();
		}
		post.setUser(this);
		posts.add(post);
	}

	public void deletePost(Post post) {
		if (post == null) {
			throw new IllegalArgumentException();
		}
		if (post.getUser().getId() == this.getId()) {
			for (Post p : post.getReposts()) {
				p.setOriginalPost(null);
			}
			posts.remove(post);
		} else {
			throw new NotOwnerException();
		}
	}

	public List<Gathering> getGatherings() {
		return gatherings;
	}

	public void createGathering(Gathering gathering) {
		if (gathering == null)
			throw new IllegalArgumentException();
		// Set owner?
		gatherings.add(gathering);
	}

	public void deleteGathering(Gathering gathering) {
		if (!gatherings.contains(gathering))
			throw new IllegalArgumentException();
		gatherings.remove(gathering);
	}

	public void follow(User user) {
		if (user == null || user.getUsername().equals(username)) {
			throw new IllegalArgumentException();
		}
		if (!followees.contains(user)) {
			Notification notification = new Notification(username
					+ " is now following you!", this, Type.FOLLOW);
			notification.notify(user);
			user.increaseFollowers();
			followees.add(user);
		}
	}

	public void unFollow(User user) {
		if (user == null || user.getUsername().equals(username)) {
			throw new IllegalArgumentException();
		}
		if (followees.contains(user)) {
			user.decreaseFollowers();
			followees.remove(user);
		}
	}

	public void favorite(Post post) {
		if (post == null) {
			throw new IllegalArgumentException();
		}
		favoritePosts.add(post);
	}

	public void unfavorite(Post post) {
		if (post == null) {
			throw new IllegalArgumentException();
		}
		// Remove all instances of post from favorites
		while (favoritePosts.remove(post)) {
		}
	}

	public boolean follows(User user) {
		return this.followees.contains(user);
	}

	public List<Notification> getNotifications() {
		return notifications;
	}

	public List<Notification> getNotificationsSorted() {
		Collections.sort(this.getNotifications());
		return notifications;
	}

	public List<User> getBlacklist() {
		return blacklist;
	}

	public List<User> getBlacklistSorted() {
		Collections.sort(this.getBlacklist());
		return blacklist;
	}

	public void blackList(User u) {
		if (!blacklist.contains(u) && u != this) {
			blacklist.add(u);
			u.unFollow(this);
			this.setViewable(false);
		} else {
			throw new IllegalArgumentException();
		}
	}

	public void unBlackList(User u) {
		if (!blacklist.remove(u)) {
			throw new IllegalArgumentException();
		}
	}

	public boolean isBlacklisting(User u) {
		return blacklist.contains(u);
	}

	public boolean isDistinguished() {
		return this.getFollowers() > JitterApplication.distinguished;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof User))
			return false;
		User other = (User) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public int compareTo(User o) {
		if (firstName.equals(o.firstName)) {
			return lastName.toLowerCase().compareTo(o.lastName.toLowerCase());
		} else {
			return firstName.toLowerCase().compareTo(o.firstName.toLowerCase());
		}
	}
}