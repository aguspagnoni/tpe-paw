package ar.edu.itba.it.paw.domain.post;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import ar.edu.itba.it.paw.domain.common.PersistentEntity;
import ar.edu.itba.it.paw.domain.hashtag.Hashtag;
import ar.edu.itba.it.paw.domain.user.User;

@Entity
@Table(name = "posts")
public class Post extends PersistentEntity {

	@ManyToOne
	private User user;
	private String body;
	private Timestamp created;

	@ManyToMany
	@Cascade(CascadeType.SAVE_UPDATE)
	private Set<Hashtag> hashtags = new HashSet<Hashtag>();

	@ManyToOne
	private Post originalPost;

	@OneToMany(mappedBy = "originalPost")
	private List<Post> reposts = new ArrayList<Post>();

	@ManyToMany
	@JoinTable(name = "favorite_posts", joinColumns = @JoinColumn(name = "post_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private List<User> favoriters = new ArrayList<User>();

	Post() {
	}

	public Post(String body) {
		setBody(body);
		created = new Timestamp(java.util.Calendar.getInstance()
				.getTimeInMillis());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Post))
			return false;
		Post other = (Post) obj;
		if (body == null) {
			if (other.body != null)
				return false;
		} else if (!body.equals(other.body))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	public String getBody() {
		return body;
	}

	public Timestamp getCreated() {
		return created;
	}

	public Set<Hashtag> getHashtags() {
		return hashtags;
	}

	public Post getOriginalPost() {
		return originalPost;
	}

	public Post getRepost() {
		Post repost = new Post(this.getBody());
		if (isRePost()) {
			repost.setOriginalPost(this.getOriginalPost());
		} else {
			repost.setOriginalPost(this);
		}
		return repost;
	}

	public List<Post> getReposts() {
		return reposts;
	}

	public User getUser() {
		return user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	public Boolean isRePost() {
		return !(originalPost == null);
	}

	public int length() {
		return body.length();
	}

	public void setBody(String body) {
		if (body == null || body.length() > 140) {
			throw new IllegalArgumentException();
		}
		this.body = body;
	}

	public void setOriginalPost(Post originalPost) {
		this.originalPost = originalPost;
	}

	public void setUser(User user) {
		if (user == null) {
			throw new IllegalArgumentException();
		}
		this.user = user;
	}

	public void addHashtag(Hashtag hashtag) {
		hashtags.add(hashtag);
		hashtag.addPost(this);
	}

}
