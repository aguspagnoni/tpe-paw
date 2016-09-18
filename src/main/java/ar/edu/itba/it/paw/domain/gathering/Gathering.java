package ar.edu.itba.it.paw.domain.gathering;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import ar.edu.itba.it.paw.domain.common.PersistentEntity;
import ar.edu.itba.it.paw.domain.user.User;

@Entity
@Table(name = "gatherings")
public class Gathering extends PersistentEntity {

	@Column(nullable = false)
	private String name;

	@ManyToMany
	@Cascade(CascadeType.SAVE_UPDATE)
	private Set<User> users = new HashSet<User>();

	@ManyToOne
	private User owner;

	Gathering() {
	}

	public Gathering(String name, User owner) {
		this.name = name;
		this.owner = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public void addUser(User user) {
		if (!users.contains(user))
			users.add(user);
	}

	public void removeUser(User user) {
		if (users.contains(user))
			users.remove(user);
	}

	public Set<User> getUsers() {
		return users;
	}
}