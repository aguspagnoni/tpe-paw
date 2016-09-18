package ar.edu.itba.it.paw.web.gathering;

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

import ar.edu.itba.it.paw.domain.gathering.Gathering;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepo;
import ar.edu.itba.it.paw.web.JitterSession;
import ar.edu.itba.it.paw.web.index.BasePage;

public class ListGatheringsPage extends BasePage {
	private static final long serialVersionUID = 6329742615589092353L;
	@SpringBean
	private UserRepo users;

	public ListGatheringsPage() {

		add(new BookmarkablePageLink<Void>("createGatheringPageLink",
				CreateGatheringPage.class));

		/**
		 * List existing gathering
		 */

		// Do checking beforehand
		JitterSession jitterSession = JitterSession.get();
		final Integer userId = jitterSession.getUserId();

		final IModel<List<Gathering>> gatheringsModel = new LoadableDetachableModel<List<Gathering>>() {
			private static final long serialVersionUID = -1152204720704062151L;

			@Override
			protected List<Gathering> load() {
				// Uncouple?
				User loggedInUser = users.get(userId);
				List<Gathering> gatheringsList = loggedInUser.getGatherings();
				return gatheringsList;
			}
		};

		ListView<Gathering> gatheringListView = new PropertyListView<Gathering>(
				"gathering", gatheringsModel) {
			private static final long serialVersionUID = -8479706071864371684L;

			@Override
			protected void populateItem(ListItem<Gathering> item) {

				item.add(new Link<Gathering>("viewGatheringLink", item
						.getModel()) {
					private static final long serialVersionUID = -2702336517530235257L;

					@Override
					public void onClick() {
						setResponsePage(ViewGatheringPage.class,
								new PageParameters().set("id", getModelObject()
										.getId()));
					}
				}.add(new Label("name")));

				item.add(new Link<Gathering>("modifyGatheringLink", item
						.getModel()) {
					private static final long serialVersionUID = -2702336517530635257L;

					@Override
					public void onClick() {
						setResponsePage(new ModifyGatheringPage(
								getModelObject()));
					}
				});

				item.add((new Link<Gathering>("deleteGatheringLink", item
						.getModel()) {
					private static final long serialVersionUID = 7849726869247569168L;

					@Override
					public void onClick() {
						Gathering gathering = getModelObject();
						Integer userId = JitterSession.get().getUserId();
						if (userId == null || !JitterSession.get().isLoggedIn()) {
							setResponsePage(ListGatheringsPage.class);
						} else {
							User user = users.get(userId);
							user.deleteGathering(gathering);
							setResponsePage(ListGatheringsPage.class);
						}
					}
				}));
			}

		};

		add(gatheringListView);

	}
}
