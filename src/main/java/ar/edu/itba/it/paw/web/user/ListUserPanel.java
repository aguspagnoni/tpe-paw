package ar.edu.itba.it.paw.web.user;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.ocpsoft.prettytime.PrettyTime;

import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.web.JitterApplication;
import ar.edu.itba.it.paw.web.user.profile.ProfilePage;

public class ListUserPanel extends Panel {
	private static final long serialVersionUID = -5931944055650920701L;

	public ListUserPanel(String id, IModel<List<User>> usersModel) {
		super(id, usersModel);

		add(new PropertyListView<User>("user", usersModel) {
			private static final long serialVersionUID = 330875056052722781L;

			@Override
			protected void populateItem(ListItem<User> item) {
				String username = item.getModelObject().getUsername();
				PageParameters params = new PageParameters();
				params.add("username", username);
				item.add(new BookmarkablePageLink<User>("profile",
						ProfilePage.class, params).add(new Label("username",
						username)));
				item.add(new Image("distinguished", JitterApplication.STAR_ICON));
				if (!item.getModelObject().isDistinguished()) {
					item.get("distinguished").setVisible(false);
				}
				item.add(new Label("firstName"));
				item.add(new Label("lastName"));
				PrettyTime p = new PrettyTime();
				item.add(new Label("created", p.format(item.getModelObject()
						.getCreated())));
			}
		});
	}

}
