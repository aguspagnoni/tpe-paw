package ar.edu.itba.it.paw.domain.post;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.DatatypeConverter;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.it.paw.domain.common.AbstractHibernateRepo;
import ar.edu.itba.it.paw.domain.gathering.Gathering;
import ar.edu.itba.it.paw.domain.hashtag.Hashtag;
import ar.edu.itba.it.paw.domain.hashtag.HashtagRepo;
import ar.edu.itba.it.paw.domain.notification.Notification;
import ar.edu.itba.it.paw.domain.notification.Notification.Type;
import ar.edu.itba.it.paw.domain.shortenedurl.ShortenedUrl;
import ar.edu.itba.it.paw.domain.shortenedurl.ShortenedUrlRepo;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepo;

@Repository
public class HibernatePostRepo extends AbstractHibernateRepo implements
		PostRepo {

	HashtagRepo hashtags;
	ShortenedUrlRepo shortenedUrls;
	UserRepo users;

	@Autowired
	public HibernatePostRepo(SessionFactory sessionFactory,
			ShortenedUrlRepo shortenedUrlRepo, HashtagRepo hashtagRepo,
			UserRepo userRepo) {
		super(sessionFactory);
		this.shortenedUrls = shortenedUrlRepo;
		this.hashtags = hashtagRepo;
		this.users = userRepo;
	}

	@Override
	public Post get(Integer id) {
		return get(Post.class, id);
	}

	@Override
	public void add(User user, Post post) {
		updateHashtags(post);
		updateUrls(post);
		notifyMentionedUsers(user, post);
		user.post(post);
	}

	private List<Post> getPostsByUsers(Collection<User> users) {
		if (users == null || users.size() == 0)
			return new ArrayList<Post>();
		Session session = getSession();
		Query query = session
				.createQuery("FROM Post post WHERE post.user IN (:users)");
		query.setParameterList("users", users);

		@SuppressWarnings("unchecked")
		List<Post> result = query.list();
		return result.size() > 0 ? result : null;
	}

	@Override
	public List<Post> getUserFeed(User user) {
		List<User> users = new ArrayList<User>();
		users.addAll(user.getFollowees());
		users.add(user);
		return getPostsByUsers(users);
	}

	@Override
	public List<Post> getAllByGathering(Gathering gathering) {
		List<User> users = new ArrayList<User>(gathering.getUsers());
		return getPostsByUsers(users);
	}

	@Override
	public List<Post> getAllByUser(User user) {
		List<Post> result = find("FROM Post post WHERE post.user = ?", user);
		return result.size() > 0 ? result : null;
	}

	@Override
	public List<Post> getAllByHashtag(Hashtag hashtag) {
		List<Post> result = find(
				"SELECT post FROM Post post JOIN post.hashtags hashtags WHERE hashtags = ?",
				hashtag);
		return result.size() > 0 ? result : null;
	}

	private void updateHashtags(Post post) {
		Pattern pattern = Pattern.compile("#([a-z0-9]+)",
				Pattern.CASE_INSENSITIVE);
		Matcher m = pattern.matcher(post.getBody());

		while (m.find()) {
			// Get corresponding Hashtag from Repo if exists, otherwise create
			String name = m.group(1);
			Hashtag hashtag = hashtags.getByName(name);

			// If doesn't exists, create and fetch
			if (hashtag == null) {
				hashtags.add(new Hashtag(name, post.getUser()));
				hashtag = hashtags.getByName(name);
			}
			post.addHashtag(hashtag);
		}
	}

	private void updateUrls(Post post) {
		// Extract Urls from post
		// Create a unique uid for the url
		// %?
		String postBody = post.getBody();
		Pattern p = Pattern.compile("(http[s]*://[\\S]+)",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(postBody);
		while (m.find()) {
			// Create a unique newUrl
			try {
				String code;
				String destination = m.group(1);
				MessageDigest md = MessageDigest.getInstance("SHA1");
				// Add currentTime to string to get a more unique hash
				// Hashing the same string twice will provide two different
				// hashes
				md.update((destination + String.valueOf(System
						.currentTimeMillis())).getBytes());
				byte[] bytes = md.digest();
				code = DatatypeConverter.printHexBinary(bytes).substring(0, 6);

				ShortenedUrl shortenedUrl = new ShortenedUrl(code, destination);
				shortenedUrls.add(shortenedUrl);

				// Swap URLs for %%code
				postBody = m.replaceFirst("%%" + code);
				post.setBody(postBody);

				m = p.matcher(postBody);
			} catch (Exception e) {
			}
		}
	}

	@Override
	public List<Post> getAllByHashtagSorted(Hashtag hashtag) {
		List<Post> posts = getAllByHashtag(hashtag);
		if (posts != null) {
			Collections.sort((List<Post>) posts, new Comparator<Post>() {

				// Note the minus sign. DESC order of date.
				@Override
				public int compare(Post o1, Post o2) {
					return -(o1.getCreated().compareTo(o2.getCreated()));
				}
			});
		}
		return posts;
	}

	public void notifyMentionedUsers(User originatingUser, Post post) {
		Set<User> mentionedUsers = extractUsers(post);
		for (User mentioneduser : mentionedUsers) {
			Notification notification = new Notification(post.getBody(),
					originatingUser, Type.MENTION);
			notification.notify(mentioneduser);
		}
	}

	@Override
	public List<Post> getAll() {
		List<Post> result = find("FROM Post post");
		return result.size() > 0 ? result : null;
	}

	private Set<User> extractUsers(Post post) {
		Set<User> mentionedUsers = new HashSet<User>();
		Pattern pattern = Pattern.compile("@(\\w)+", Pattern.CASE_INSENSITIVE);
		Matcher m = pattern.matcher(post.getBody());
		while (m.find()) {
			String username = m.group().substring(1);
			User user = users.getByName(username);
			if (user != null) {
				mentionedUsers.add(user);
			}
		}
		return mentionedUsers;
	}
}
