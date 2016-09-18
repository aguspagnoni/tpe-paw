package ar.edu.itba.it.paw.domain;

import org.junit.Assert;
import org.junit.Test;

import ar.edu.itba.it.paw.domain.notification.Notification;
import ar.edu.itba.it.paw.domain.notification.Notification.Type;
import ar.edu.itba.it.paw.domain.user.User;

public class NotificationTest {

	@Test
	public void sendNotificationTest() {
		User user = new User("username", "password", "first", "last", "q", "a",
				"desc", 0, 0, true);
		Notification notification = new Notification("notification", user,
				Type.FOLLOW);
		notification.notify(user);
		Assert.assertEquals(user.getNotifications().size(), 1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createNotificationFromNullUserTest() {
		new Notification("notification", null, Type.FOLLOW);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createNotificationToNullUserTest() {
		User user = new User("username", "password", "first", "last", "q", "a",
				"desc", 0, 0, true);
		Notification notification = new Notification("notification", user,
				Type.FOLLOW);
		notification.notify(null);
	}
}
