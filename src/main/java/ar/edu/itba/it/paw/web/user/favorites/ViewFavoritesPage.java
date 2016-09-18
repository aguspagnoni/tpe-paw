package ar.edu.itba.it.paw.web.user.favorites;

import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.post.Post;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepo;
import ar.edu.itba.it.paw.web.JitterSession;
import ar.edu.itba.it.paw.web.index.BasePage;
import ar.edu.itba.it.paw.web.post.ListPostsPanel;

public class ViewFavoritesPage extends BasePage {

	private static final long serialVersionUID = 1203614101003915666L;
	@SpringBean
	UserRepo users;

	public ViewFavoritesPage() {

		if (JitterSession.get().isLoggedIn()) {
			final IModel<List<Post>> postsModel = new LoadableDetachableModel<List<Post>>() {
				private static final long serialVersionUID = -1152204720704062151L;

				@Override
				protected List<Post> load() {
					User user = users.get(JitterSession.get().getUserId());
					return new LinkedList<Post>(user.getFavoritePosts());
				}
			};

			add(new ListPostsPanel("postListing", postsModel));

		}
	}
}
