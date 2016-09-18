package ar.edu.itba.it.paw.web.hashtag;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.hashtag.HashtagRepo;
import ar.edu.itba.it.paw.domain.hashtag.RankedHashtag;
import ar.edu.itba.it.paw.domain.user.UserRepo;

public class ListHashtagsPanel extends Panel {

	@SpringBean
	UserRepo users;
	@SpringBean
	HashtagRepo hashtags;

	private static final long serialVersionUID = -7683832554000111674L;

	public ListHashtagsPanel(String id,
			IModel<List<RankedHashtag>> hashtagsModel) {
		super(id, hashtagsModel);

		ListView<RankedHashtag> listview = new PropertyListView<RankedHashtag>(
				"hashtag", hashtagsModel) {
			private static final long serialVersionUID = -2137739996917682157L;

			@Override
			protected void populateItem(ListItem<RankedHashtag> item) {
				item.add(new Label("rank"));
				item.add(new BookmarkablePageLink<RankedHashtag>("hashtagLink",
						ViewHashtagPage.class, new PageParameters().set(
								"hashtagName", item.getModelObject()
										.getHashtag().getName()))
						.add(new Label("hashtag.name")));
			}
		};
		add(listview);
	}
}
