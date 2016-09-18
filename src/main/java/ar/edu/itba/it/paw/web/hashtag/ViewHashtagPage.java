package ar.edu.itba.it.paw.web.hashtag;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.hashtag.Hashtag;
import ar.edu.itba.it.paw.domain.hashtag.HashtagRepo;
import ar.edu.itba.it.paw.domain.post.Post;
import ar.edu.itba.it.paw.domain.post.PostRepo;
import ar.edu.itba.it.paw.domain.user.UserRepo;
import ar.edu.itba.it.paw.web.index.BasePage;
import ar.edu.itba.it.paw.web.post.ListPostsPanel;

public class ViewHashtagPage extends BasePage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SpringBean
	private UserRepo users;
	@SpringBean
	private PostRepo posts;
	@SpringBean
	private HashtagRepo hashtags;

	public ViewHashtagPage(final PageParameters parameters) {
		super();
		final String hashtagName = parameters.get("hashtagName").toString();

		IModel<List<Post>> postsModel = new LoadableDetachableModel<List<Post>>() {
			private static final long serialVersionUID = -1152204720704062151L;

			@Override
			protected List<Post> load() {
				Hashtag hashtag = hashtags.getByName(hashtagName);
				List<Post> postsList = posts.getAllByHashtagSorted(hashtag);
				return postsList;
			}
		};

		IModel<Hashtag> hashtagModel = new LoadableDetachableModel<Hashtag>() {
			private static final long serialVersionUID = -1151204720704062151L;

			@Override
			protected Hashtag load() {
				return hashtags.getByName(hashtagName);
			}
		};

		add(new Label("hashtagName", new PropertyModel<Hashtag>(hashtagModel,
				"name")));
		add(new Label("creatorUsername", new PropertyModel<Hashtag>(
				hashtagModel, "creator.username")));
		add(new Label("created", new PropertyModel<Hashtag>(hashtagModel,
				"created")));

		add(new ListPostsPanel("postListing", postsModel));
	}
}
