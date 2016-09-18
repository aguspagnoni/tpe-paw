package ar.edu.itba.it.paw.web.user;

import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepo;
import ar.edu.itba.it.paw.web.index.BasePage;

public class ListMatchingUsersPage extends BasePage {
	private static final long serialVersionUID = 5508576635124917333L;
	@SpringBean
	private UserRepo users;

	public ListMatchingUsersPage(final String searchstring) {
		IModel<List<User>> usersModel = new LoadableDetachableModel<List<User>>() {
			private static final long serialVersionUID = -3877591959552689216L;

			@Override
			protected List<User> load() {
				return (List<User>) users.getMatchingSorted(searchstring);
			}
		};

		add(new ListUserPanel("matchingUsersList", usersModel));
	}
}
