package ar.edu.itba.it.paw.domain;

import org.junit.Assert;
import org.junit.Test;

import ar.edu.itba.it.paw.domain.post.Post;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.exception.InvalidPasswordException;
import ar.edu.itba.it.paw.exception.InvalidUsernameException;

public class UserTest {

	@Test(expected = InvalidUsernameException.class)
	public void invalidUsernameTest() {
		new User("!@#", "password", "first", "last", "q", "a", "desc", 0, 0,
				true);
	}

	@Test(expected = InvalidPasswordException.class)
	public void emptyPasswordTest() {
		new User("username", "", "first", "last", "q", "a", "desc", 0, 0, true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullAnyParameterTest() {
		new User(null, null, null, null, null, null, null, 0, 0, null);
	}

	@Test
	public void okTest() {
		new User("username", "password", "first", "last", "q", "a", "desc", 0,
				0, true);
	}

	@Test
	public void userStartsWithNoPostsTest() {
		User user = new User("username", "password", "first", "last", "q", "a",
				"desc", 0, 0, true);
		Assert.assertEquals(user.getPosts().size(), 0);
	}

	@Test
	public void addPostTest() {
		User user = new User("username", "password", "first", "last", "q", "a",
				"desc", 0, 0, true);
		user.post(new Post("postbody"));
		Assert.assertEquals(user.getPosts().size(), 1);
	}

	@Test
	public void removePostTest() {
		User user = new User("username", "password", "first", "last", "q", "a",
				"desc", 0, 0, true);
		Post post = new Post("postbody");
		user.post(post);
		user.deletePost(post);
		Assert.assertEquals(user.getPosts().size(), 0);
	}

	@Test
	public void changePasswordTest() {
		User user = new User("username", "password", "first", "last", "q", "a",
				"desc", 0, 0, true);
		user.setPassword("newpassword");
	}

	@Test
	public void followUserTest() {
		User user1 = new User("username1", "password", "first", "last", "q",
				"a", "desc", 0, 0, true);
		User user2 = new User("username2", "password", "first", "last", "q",
				"a", "desc", 0, 0, true);
		user1.follow(user2);
		Assert.assertEquals(user1.getFollowees().size(), 1);
	}

	@Test
	public void unFollowUserTest() {
		User user1 = new User("username1", "password", "first", "last", "q",
				"a", "desc", 0, 0, true);
		User user2 = new User("username2", "password", "first", "last", "q",
				"a", "desc", 0, 0, true);
		user1.follow(user2);
		user1.unFollow(user2);
		Assert.assertEquals(user1.getFollowees().size(), 0);
	}

	@Test
	public void followedUserIncreasesFollowerCountTest() {
		User user1 = new User("username1", "password", "first", "last", "q",
				"a", "desc", 0, 0, true);
		User user2 = new User("username2", "password", "first", "last", "q",
				"a", "desc", 0, 0, true);
		user1.follow(user2);
		Assert.assertEquals(user2.getFollowers(), 1);
	}

	@Test
	public void receiveFollowNotificationTest() {
		User user1 = new User("username1", "password", "first", "last", "q",
				"a", "desc", 0, 0, true);
		User user2 = new User("username2", "password", "first", "last", "q",
				"a", "desc", 0, 0, true);
		user1.follow(user2);
		Assert.assertEquals(user2.getNotifications().size(), 1);
	}

	@Test
	public void favoriteTest() {
		User user1 = new User("username1", "password", "first", "last", "q",
				"a", "desc", 0, 0, true);
		User user2 = new User("username2", "password", "first", "last", "q",
				"a", "desc", 0, 0, true);
		Post post = new Post("postbody");
		user2.post(post);
		user1.favorite(post);
		Assert.assertEquals(user1.getFavoritePosts().size(), 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void cannotFollowSelfTest() {
		User user1 = new User("username1", "password", "first", "last", "q",
				"a", "desc", 0, 0, true);
		user1.follow(user1);
	}
}
