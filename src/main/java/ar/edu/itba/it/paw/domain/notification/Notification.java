package ar.edu.itba.it.paw.domain.notification;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.edu.itba.it.paw.domain.common.PersistentEntity;
import ar.edu.itba.it.paw.domain.user.User;

@Entity
@Table(name = "notifications")
public class Notification extends PersistentEntity implements
		Comparable<Notification> {

	public enum Type {
		MENTION("Mention"), REPOST("Repost"), FOLLOW("Follow");
		private String value;

		private Type(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	@Column(nullable = false)
	private String message;

	@ManyToOne
	private User user;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Type type;

	@Column(nullable = false)
	private Timestamp created;

	Notification() {
	}

	public Notification(String message, User user, Type type) {
		if (message == null || user == null || type == null) {
			throw new IllegalArgumentException();
		}
		this.message = message;
		this.user = user;
		this.type = type;
		this.created = new Timestamp(java.util.Calendar.getInstance()
				.getTimeInMillis());
	}

	@Override
	public int compareTo(Notification o) {
		// reverse order intended, newer notifications first
		return o.created.compareTo(created);
	}

	public Timestamp getCreated() {
		return created;
	}

	public String getMessage() {
		return message;
	}

	public Type getType() {
		return type;
	}

	public User getUser() {
		return user;
	}

	public void notify(User user) {
		if (user == null) {
			throw new IllegalArgumentException();
		}
		user.getNotifications().add(this);
	}
}
