package ar.edu.itba.it.paw.web.gathering;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.common.EntityResolver;
import ar.edu.itba.it.paw.domain.gathering.Gathering;
import ar.edu.itba.it.paw.domain.post.Post;
import ar.edu.itba.it.paw.domain.post.PostRepo;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.web.index.BasePage;
import ar.edu.itba.it.paw.web.post.ListPostsPanel;
import ar.edu.itba.it.paw.web.user.profile.ProfilePage;

public class ViewGatheringPage extends BasePage {
	private static final long serialVersionUID = -2079830910284142492L;

	@SpringBean
	EntityResolver entityResolver;

	@SpringBean
	PostRepo posts;

	public ViewGatheringPage(final PageParameters pp) {
		final Integer gatheringId = pp.get("id").toInteger();

		// Display users in gathering
		final IModel<List<User>> usersModel = new LoadableDetachableModel<List<User>>() {
			private static final long serialVersionUID = -7919566892383483599L;

			@Override
			protected List<User> load() {
				Gathering gathering = entityResolver.fetch(Gathering.class,
						gatheringId);
				return new ArrayList<User>(gathering.getUsers());
			}
		};

		ListView<User> userListView = new PropertyListView<User>("user",
				usersModel) {
			private static final long serialVersionUID = -3001326726063673037L;

			@Override
			protected void populateItem(ListItem<User> item) {

				item.add(new BookmarkablePageLink<User>("profilePageLink",
						ProfilePage.class, new PageParameters().set("username",
								item.getModelObject().getUsername()))
						.add(new Label("username")));

				// Remove user from list link
				item.add(new Link<User>("removeUserFromGatheringLink", item
						.getModel()) {
					private static final long serialVersionUID = -4746004669969125955L;

					@Override
					public void onClick() {
						Gathering gathering = entityResolver.fetch(
								Gathering.class, gatheringId);
						gathering.removeUser(getModelObject());
						setResponsePage(ViewGatheringPage.class, pp);
					}

				});

			}

		};

		add(userListView);

		// Display messages posted by users in Gathering

		final IModel<List<Post>> postsModel = new LoadableDetachableModel<List<Post>>() {
			private static final long serialVersionUID = -1152204720704062151L;

			@Override
			protected List<Post> load() {
				Gathering gathering = entityResolver.fetch(Gathering.class,
						gatheringId);
				return posts.getAllByGathering(gathering);
			}
		};

		add(new ListPostsPanel("postListing", postsModel));

	}
}
