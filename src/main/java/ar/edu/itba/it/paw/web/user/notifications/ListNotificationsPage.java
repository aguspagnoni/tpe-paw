package ar.edu.itba.it.paw.web.user.notifications;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.ocpsoft.prettytime.PrettyTime;

import ar.edu.itba.it.paw.domain.notification.Notification;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepo;
import ar.edu.itba.it.paw.web.JitterApplication;
import ar.edu.itba.it.paw.web.index.BasePage;
import ar.edu.itba.it.paw.web.user.profile.ProfilePage;

public class ListNotificationsPage extends BasePage {
	private static final long serialVersionUID = 8671252325459465689L;
	@SpringBean
	UserRepo users;

	private static final String baseUrl = "/jitter/bin";
	
	public ListNotificationsPage(final String username) {

		IModel<List<Notification>> notificationsModel = new LoadableDetachableModel<List<Notification>>() {
			private static final long serialVersionUID = 1720789067127768769L;

			@Override
			protected List<Notification> load() {
				return users.getByName(username).getNotificationsSorted();
			}
		};

		add(new PropertyListView<Notification>("notification",
				notificationsModel) {
			private static final long serialVersionUID = 9217412326877374758L;

			@Override
			protected void populateItem(ListItem<Notification> item) {

				PrettyTime p = new PrettyTime();
				item.add(new Label("created", p.format(item.getModelObject()
						.getCreated())));
				String username = item.getModelObject().getUser().getUsername();
				PageParameters params = new PageParameters();
				params.add("username", username);
				item.add(new BookmarkablePageLink<User>("profile",
						ProfilePage.class, params).add(new Label("username",
						username)));

				//item.getModelObject().getMessage();

				IModel<String> postBodyModel = new IModel<String>() {
					private static final long serialVersionUID = -251596819220511067L;
					private String body;

					@Override
					public void detach() {
					}

					@Override
					public String getObject() {
						// Parse body
						String bodyParsed;
						bodyParsed = body.replaceAll(
								"(.*?)@([a-zA-Z0-9]+)(.*?)", "$1<a href=\""
										+ baseUrl + "/profile/$2\">@$2</a>$3");
						bodyParsed = bodyParsed.replaceAll(
								"(.*?)#([a-zA-Z0-9]+)(.*?)", "$1<a href=\""
										+ baseUrl + "/hashtag/$2\">#$2</a>$3");
						bodyParsed = bodyParsed
								.replaceAll(
										"(.*?)%%([\\S]+)(.*?)",
										"$1<a href=\""
												+ baseUrl
												+ "/s/v/$2\" target=\"_blank\">LINK</a>$3");

						return bodyParsed;
					}

					@Override
					public void setObject(String body) {
						this.body = body;
					}

				};

				postBodyModel.setObject(item.getModelObject().getMessage());

				item.add(new Label("message", postBodyModel)
						.setEscapeModelStrings(false));
				
				//item.add(new Label("message"));
				item.add(new Label("type"));
				item.add(new Image("distinguished", JitterApplication.STAR_ICON));
				if (!item.getModelObject().getUser().isDistinguished()) {
					item.get("distinguished").setVisible(false);
				}
			}
		});
	}
}
