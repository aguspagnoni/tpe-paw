package ar.edu.itba.it.paw.web.user.profile.followees;

import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepo;
import ar.edu.itba.it.paw.web.index.BasePage;
import ar.edu.itba.it.paw.web.user.ListUserPanel;

public class ListFolloweesPage extends BasePage {
	private static final long serialVersionUID = -6781973891453946593L;
	@SpringBean
	UserRepo users;

	public ListFolloweesPage(final String username) {
		IModel<List<User>> usersModel = new LoadableDetachableModel<List<User>>() {
			private static final long serialVersionUID = 3847969645710737467L;

			@Override
			protected List<User> load() {
				return (List<User>) users.getByName(username).getFollowees();
			}
		};

		add(new ListUserPanel("followeesList", usersModel));
	}
}
