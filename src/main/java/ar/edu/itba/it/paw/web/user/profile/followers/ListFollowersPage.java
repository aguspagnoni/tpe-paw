package ar.edu.itba.it.paw.web.user.profile.followers;

import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepo;
import ar.edu.itba.it.paw.web.index.BasePage;
import ar.edu.itba.it.paw.web.user.ListUserPanel;

public class ListFollowersPage extends BasePage {
	private static final long serialVersionUID = 5865810392240309680L;
	@SpringBean
	UserRepo users;

	public ListFollowersPage(final String username) {

		IModel<List<User>> usersModel = new LoadableDetachableModel<List<User>>() {
			private static final long serialVersionUID = 5673790812242100100L;

			@Override
			protected List<User> load() {
				return (List<User>) users.getFollowers(username);
			}
		};

		add(new ListUserPanel("followersList", usersModel));
	}
}
