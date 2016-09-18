package ar.edu.itba.it.paw.domain.post;

import java.util.List;

import ar.edu.itba.it.paw.domain.gathering.Gathering;
import ar.edu.itba.it.paw.domain.hashtag.Hashtag;
import ar.edu.itba.it.paw.domain.user.User;

public interface PostRepo {

	public Post get(Integer id);

	public void add(User user, Post post);

	public List<Post> getAll();

	public List<Post> getUserFeed(User user);

	public List<Post> getAllByUser(User user);

	public List<Post> getAllByHashtag(Hashtag hashtag);

	public List<Post> getAllByGathering(Gathering gathering);

	public List<Post> getAllByHashtagSorted(Hashtag hashtag);

	public void notifyMentionedUsers(User user, Post post);
}
