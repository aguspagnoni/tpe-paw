package ar.edu.itba.it.paw.web.user.profile;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.gathering.Gathering;
import ar.edu.itba.it.paw.domain.post.Post;
import ar.edu.itba.it.paw.domain.post.PostRepo;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepo;
import ar.edu.itba.it.paw.web.JitterSession;
import ar.edu.itba.it.paw.web.gathering.ViewGatheringPage;
import ar.edu.itba.it.paw.web.index.BasePage;
import ar.edu.itba.it.paw.web.post.ListPostsPanel;
import ar.edu.itba.it.paw.web.user.profile.followees.ListFolloweesPage;
import ar.edu.itba.it.paw.web.user.profile.followers.ListFollowersPage;

public class ProfilePage extends BasePage {
	private static final long serialVersionUID = -4166138860862583004L;
	@SpringBean
	UserRepo users;
	@SpringBean
	PostRepo posts;
	JitterSession session = JitterSession.get();

	private transient Gathering gathering;

	public ProfilePage(final PageParameters parameters) {
		final String username = parameters.get("username").toString();
		User userBeingViewed = users.getByName(username);
		if (userBeingViewed == null) {
			setResponsePage(getApplication().getHomePage());
			return;
		}
		// Adding / Deleting user from a gathering
		Form<ProfilePage> form = new Form<ProfilePage>("gatheringsForm",
				new CompoundPropertyModel<ProfilePage>(this));
		add(form);

		form.add(new DropDownChoice<Gathering>("gathering",
				new LoadableDetachableModel<List<Gathering>>() {
					private static final long serialVersionUID = -6765186142534855592L;

					@Override
					protected List<Gathering> load() {
						User currentUser = users.getByName(session
								.getUsername());
						return currentUser.getGatherings();
					}
				}, new ChoiceRenderer<Gathering>("name")));
		form.add(new Button("addUserToGathering") {
			private static final long serialVersionUID = -4369879014661133042L;

			@Override
			public void onSubmit() {
				User userBeingViewed = users.getByName(username);
				gathering.addUser(userBeingViewed);
				info("User added to gathering.");
				setResponsePage(ViewGatheringPage.class,
						new PageParameters().set("id", gathering.getId()));
			}
		});

		// End snippet

		if (session.getUsername() != userBeingViewed.getUsername()) {
			userBeingViewed.increaseVisits();
		}

		IModel<List<Post>> postsModel = new LoadableDetachableModel<List<Post>>() {
			private static final long serialVersionUID = 5673790812242100100L;

			@Override
			protected List<Post> load() {
				return posts.getAllByUser(users.getByName(username));
			}
		};

		add(new ListPostsPanel("postListing", postsModel));

		add(new FeedbackPanel("errorPanel"));

		add(new Label("private",
				"This Profile is private, you need to log in to see it."));
		add(new Label("blacklisted",
				"You do not have permission to view this profile."));
		add(new Label("username", "Username: " + userBeingViewed.getUsername()));
		add(new Label("firstName", "First Name: "
				+ userBeingViewed.getFirstName()));
		add(new Label("lastName", "Last Name: " + userBeingViewed.getLastName()));
		add(new Label("description", "Description: "
				+ userBeingViewed.getDescription()));
		add(new Label("profileVisits", "Profile Visits: "
				+ userBeingViewed.getProfileVisits()));
		add(new Label("followercount", "Followers: "
				+ userBeingViewed.getFollowers()));
		add(new Label("followeecount", "Followees: "
				+ userBeingViewed.getFollowees().size()));
		add(new Label("viewable", "Privacy : "
				+ (userBeingViewed.getViewable() ? "Public" : "Private")));

		add(new Link<Void>("follow") {
			private static final long serialVersionUID = -6236269616541083838L;

			@Override
			public void onClick() {
				User currentuser = users.getByName(session.getUsername());
				try {
					currentuser.follow(users.getByName(username));
					setResponsePage(new ProfilePage(parameters));
				} catch (IllegalArgumentException e) {
					error("You cannot follow yourself.");
				}
			}
		});

		add(new Link<Void>("unFollow") {
			private static final long serialVersionUID = -4400643395345929368L;

			@Override
			public void onClick() {
				User currentuser = users.getByName(session.getUsername());
				try {
					currentuser.unFollow(users.getByName(username));
					setResponsePage(new ProfilePage(parameters));
				} catch (IllegalArgumentException e) {
					error("You cannot unfollow yourself.");
				}
			}
		});

		add(new Link<Void>("listFollowersPageLink") {
			private static final long serialVersionUID = 8114879237295232738L;

			@Override
			public void onClick() {
				setResponsePage(new ListFollowersPage(username));
			}
		});

		add(new Link<Void>("listFolloweesPageLink") {
			private static final long serialVersionUID = 2308456499541583197L;

			@Override
			public void onClick() {
				setResponsePage(new ListFolloweesPage(username));
			}
		});

		String currentUserUsername = session.getUsername();

		if (session.isLoggedIn()) {
			if (userBeingViewed.isBlacklisting(users
					.getByName(currentUserUsername))) {
				get("username").setVisible(false);
				get("firstName").setVisible(false);
				get("lastName").setVisible(false);
				get("description").setVisible(false);
				get("profileVisits").setVisible(false);
				get("followercount").setVisible(false);
				get("followeecount").setVisible(false);
				get("viewable").setVisible(false);
				get("unFollow").setVisible(false);
				get("follow").setVisible(false);
				get("postListing").setVisible(false);
				get("listFollowersPageLink").setVisible(false);
				get("listFolloweesPageLink").setVisible(false);
				get("gatheringsForm").setVisible(false);
			} else {
				if (currentUserUsername.equals(userBeingViewed.getUsername())
						|| users.getByName(currentUserUsername).follows(
								userBeingViewed)) {
					get("follow").setVisible(false);
				}
				if (!users.getByName(currentUserUsername).follows(
						userBeingViewed)) {
					get("unFollow").setVisible(false);
				}
				get("blacklisted").setVisible(false);
			}
			get("private").setVisible(false);
		} else {// not logged in
			get("gatheringsForm").setVisible(false);
			get("unFollow").setVisible(false);
			get("follow").setVisible(false);
			if (userBeingViewed.getViewable()) {
				get("private").setVisible(false);
			} else {
				get("username").setVisible(false);
				get("firstName").setVisible(false);
				get("lastName").setVisible(false);
				get("description").setVisible(false);
				get("profileVisits").setVisible(false);
				get("followercount").setVisible(false);
				get("followeecount").setVisible(false);
				get("viewable").setVisible(false);
				get("postListing").setVisible(false);
				get("listFollowersPageLink").setVisible(false);
				get("listFolloweesPageLink").setVisible(false);
			}
			get("blacklisted").setVisible(false);
		}
	}
}
