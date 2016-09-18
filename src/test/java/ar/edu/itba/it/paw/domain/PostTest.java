package ar.edu.itba.it.paw.domain;

import org.junit.Assert;
import org.junit.Test;

import ar.edu.itba.it.paw.domain.post.Post;
import ar.edu.itba.it.paw.domain.user.User;

public class PostTest {

	@Test(expected = IllegalArgumentException.class)
	public void tooLongTest() {
		new Post(
				"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
	}

	@Test
	public void newPostIsNotRepostTest() {
		Post post = new Post("body");
		Assert.assertFalse(post.isRePost());
	}

	@Test
	public void isRepostTest() {
		Post post = new Post("body");
		Post repost = post.getRepost();
		Assert.assertTrue(repost.isRePost());
	}

	@Test
	public void rePostPreservesOriginalAuthorTest() {
		Post post = new Post("body");
		User user = new User("username", "password", "first", "last", "q", "a",
				"desc", 0, 0, true);
		post.setUser(user);
		Post repost = post.getRepost();
		Assert.assertTrue(repost.getOriginalPost().getUser()
				.equals(post.getUser()));
	}
}
