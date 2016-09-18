package ar.edu.itba.it.paw.web.index;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.Cookie;

import org.apache.wicket.Session;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.Strings;

import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepo;
import ar.edu.itba.it.paw.web.JitterSession;
import ar.edu.itba.it.paw.web.gathering.ListGatheringsPage;
import ar.edu.itba.it.paw.web.post.CreatePostPage;
import ar.edu.itba.it.paw.web.user.ListMatchingUsersPage;
import ar.edu.itba.it.paw.web.user.blacklist.ListBlacklistPage;
import ar.edu.itba.it.paw.web.user.favorites.ViewFavoritesPage;
import ar.edu.itba.it.paw.web.user.notifications.ListNotificationsPage;
import ar.edu.itba.it.paw.web.user.profile.EditProfilePage;

public class NavigationPanel extends Panel {

	@SpringBean
	private UserRepo users;
	private transient String searchstring;

	public NavigationPanel(String id) {
		super(id);

		JitterSession session = JitterSession.get();

		String loggedInUsername = (String) Session.get().getAttribute(
				"loggedinusername");

		add(new BookmarkablePageLink<Void>("usernameLink", IndexPage.class)
				.add(new Label("username", loggedInUsername)));
		final String username = session.getUsername();
		// Include links to different pages
		add(new BookmarkablePageLink<Void>("indexPageLink", IndexPage.class));
		add(new Link<Void>("accountInfoPageLink") {
			private static final long serialVersionUID = 7613405459628291037L;

			@Override
			public void onClick() {
				setResponsePage(new EditProfilePage(users.getByName(username)));
			}
		});
		add(new BookmarkablePageLink<Void>("createPostPageLink",
				CreatePostPage.class));
		add(new Link<Void>("listNotificationsPageLink") {
			private static final long serialVersionUID = 3491056839649271560L;

			@Override
			public void onClick() {
				setResponsePage(new ListNotificationsPage(username));
			}
		});

		add(new BookmarkablePageLink<Void>("postFavoritesPageLink",
				ViewFavoritesPage.class));

		add(new BookmarkablePageLink<Void>("listGatheringsPageLink",
				ListGatheringsPage.class));

		add(new Link<Void>("listBlacklistPageLink") {
			private static final long serialVersionUID = -359757429397655489L;

			@Override
			public void onClick() {
				setResponsePage(new ListBlacklistPage());
			}
		});

		add(new Link<Void>("userLogoutLink") {
			private static final long serialVersionUID = -7373157471782851074L;

			@Override
			public void onClick() {
				User user = users.get(JitterSession.get().getUserId());
				user.clearToken();
				Cookie clearUsername = new Cookie("username", "");
				Cookie clearToken = new Cookie("token", "");
				clearUsername.setMaxAge(0);
				clearToken.setMaxAge(0);
				((WebResponse) getRequestCycle().getResponse())
						.addCookie(clearUsername);
				((WebResponse) getRequestCycle().getResponse())
						.addCookie(clearToken);
				JitterSession.get().logOut();
				setResponsePage(IndexPage.class);
			}
		});

		if (!session.isLoggedIn()) {
			get("usernameLink").setVisible(false);
			get("accountInfoPageLink").setVisible(false);
			get("createPostPageLink").setVisible(false);
			get("listNotificationsPageLink").setVisible(false);
			get("postFavoritesPageLink").setVisible(false);
			get("listGatheringsPageLink").setVisible(false);
			get("listBlacklistPageLink").setVisible(false);
			get("userLogoutLink").setVisible(false);
		}

		final IModel<String> model = new IModel<String>() {
			private static final long serialVersionUID = 5842698779635226910L;

			@Override
			public String getObject() {
				return searchstring;
			}

			@Override
			public void setObject(String object) {
				searchstring = object;
			}

			@Override
			public void detach() {
			}
		};

		Form<NavigationPanel> userSearchForm = new Form<NavigationPanel>(
				"userSearchForm");

		userSearchForm.add(new AutoCompleteTextField<String>("searchText",
				model) {
			private static final long serialVersionUID = 1L;

			@Override
			protected Iterator<String> getChoices(String input) {
				if (Strings.isEmpty(input)) {
					List<String> emptyList = Collections.emptyList();
					return emptyList.iterator();
				}
				List<String> usernamelist = new ArrayList<String>();
				Iterable<User> userlist = users.getAllMatchingUsers(input);
				for (final User u : userlist) {
					usernamelist.add(u.getUsername());
				}
				return usernamelist.iterator();
			}
		});

		userSearchForm.add(new Button("find") {
			private static final long serialVersionUID = 86965334644163022L;

			@Override
			public void onSubmit() {
				super.onSubmit();
				setResponsePage(new ListMatchingUsersPage(searchstring));
			}
		});
		add(userSearchForm);

	}

	private static final long serialVersionUID = 3846388287642539508L;
}
