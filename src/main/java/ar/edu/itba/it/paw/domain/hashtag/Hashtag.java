package ar.edu.itba.it.paw.domain.hashtag;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import ar.edu.itba.it.paw.domain.common.PersistentEntity;
import ar.edu.itba.it.paw.domain.post.Post;
import ar.edu.itba.it.paw.domain.user.User;

/**
 * Represents an application hashtag.
 */
@Entity
@Table(name = "hashtags")
public class Hashtag extends PersistentEntity {

	@Column(unique = true)
	private String name;
	private Timestamp created;

	@OneToOne
	private User creator;

	@ManyToMany(mappedBy = "hashtags")
	private Set<Post> posts = new HashSet<Post>();

	Hashtag() {
	}

	public Hashtag(String name, User creator) {
		super();
		this.name = name;
		this.creator = creator;
		this.created = new Timestamp(java.util.Calendar.getInstance()
				.getTimeInMillis());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Hashtag))
			return false;
		Hashtag other = (Hashtag) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public Timestamp getCreated() {
		return created;
	}

	public User getCreator() {
		return creator;
	}

	public String getName() {
		return name;
	}

	public Set<Post> getPosts() {
		return posts;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	public void addPost(Post post) {
		posts.add(post);
	}
}
