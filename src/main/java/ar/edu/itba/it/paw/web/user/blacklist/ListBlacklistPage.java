package ar.edu.itba.it.paw.web.user.blacklist;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepo;
import ar.edu.itba.it.paw.web.JitterApplication;
import ar.edu.itba.it.paw.web.JitterSession;
import ar.edu.itba.it.paw.web.common.DeleteLink;
import ar.edu.itba.it.paw.web.index.BasePage;

public class ListBlacklistPage extends BasePage {
	private static final long serialVersionUID = 1732839093592218462L;
	@SpringBean
	private UserRepo users;
	private transient String username;

	public ListBlacklistPage() {

		final IModel<List<User>> usersModel = new LoadableDetachableModel<List<User>>() {
			private static final long serialVersionUID = -1842754768322828377L;

			@Override
			protected List<User> load() {
				return users.getByName(JitterSession.get().getUsername())
						.getBlacklist();
			}
		};

		ListView<User> listview = new PropertyListView<User>(
				"blacklisted_user", usersModel) {
			private static final long serialVersionUID = 8516626623962910120L;

			@Override
			protected void populateItem(ListItem<User> item) {
				item.add(new Label("username"));
				item.add(new DeleteLink<User>("remove", item.getModel()) {
					private static final long serialVersionUID = -6603925721505212926L;

					@Override
					public void onClick() {
						User u = getModelObject();
						users.getByName(JitterSession.get().getUsername())
								.unBlackList(u);
						usersModel.detach();
					}
				});
				item.add(new Image("distinguished", JitterApplication.STAR_ICON));
				if (!item.getModelObject().isDistinguished()) {
					item.get("distinguished").setVisible(false);
				}
			}
		};
		add(listview);

		add(new FeedbackPanel("feedback"));
		add(new AddUserForm("form"));
	}

	private class AddUserForm extends Form<Void> {
		private static final long serialVersionUID = 1L;

		final IModel<String> model = new IModel<String>() {
			private static final long serialVersionUID = 1997586763611468889L;

			@Override
			public String getObject() {
				return username;
			}

			@Override
			public void setObject(String object) {
				username = object;
			}

			@Override
			public void detach() {
			}
		};

		public AddUserForm(String id) {
			super(id);
			add(new TextField<String>("username", model).setRequired(true));

			add(new SubmitLink("add") {
				private static final long serialVersionUID = -2567745971109214942L;

				@Override
				public void onSubmit() {
					User currentUser = users.getByName(JitterSession.get()
							.getUsername());
					User blacklistedUser = users.getByName(username);
					if (blacklistedUser == null) {
						error("Username not found");
					}
					if (!hasError()) {
						try {
							currentUser.blackList(blacklistedUser);
							username = "";
						} catch (IllegalArgumentException e) {
							error("Username error");
						}

					}
				};
			}.add(new Image("addImage", JitterApplication.ADD_ICON)));
		}
	}
}
