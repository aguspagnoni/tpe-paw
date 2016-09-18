package ar.edu.itba.it.paw.web.index;

import java.util.List;

import javax.servlet.http.Cookie;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.hashtag.HashtagRepo;
import ar.edu.itba.it.paw.domain.hashtag.RankedHashtag;
import ar.edu.itba.it.paw.domain.post.Post;
import ar.edu.itba.it.paw.domain.post.PostRepo;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepo;
import ar.edu.itba.it.paw.web.JitterSession;
import ar.edu.itba.it.paw.web.hashtag.ListHashtagsPanel;
import ar.edu.itba.it.paw.web.post.ListPostsPanel;
import ar.edu.itba.it.paw.web.user.recoverPassword.RecoverPasswordPage;
import ar.edu.itba.it.paw.web.user.register.RegisterUserPage;

public class IndexPage extends BasePage {

	private static final long serialVersionUID = 7546390487753726393L;
	@SpringBean
	private UserRepo users;
	@SpringBean
	private PostRepo posts;
	@SpringBean
	private HashtagRepo hashtags;

	private transient String username;
	private transient String password;
	private transient Boolean rememberme;

	public IndexPage() {
		super();

		JitterSession session = JitterSession.get();

		add(new FeedbackPanel("loginFeedback"));

		Form<IndexPage> loginForm = new Form<IndexPage>("loginForm",
				new CompoundPropertyModel<IndexPage>(this)) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				JitterSession session = JitterSession.get();
				if (session.logIn(username, password, users)) {
					continueToOriginalDestination();
					setResponsePage(getApplication().getHomePage());
					// If rememberme was set, save generate magic key and save
					// for User and in cookie
					if (rememberme == true) {
						String token = RandomStringUtils
								.random(100, true, true);
						User user = users.get(session.getUserId());
						user.setToken(token);
						((WebResponse) getRequestCycle().getResponse())
								.addCookie(new Cookie("username", user
										.getUsername()));
						((WebResponse) getRequestCycle().getResponse())
								.addCookie(new Cookie("token", token));
					}

				} else {
					error(getString("invalidCredentials"));
				}
			}
		};

		loginForm.add(new TextField<String>("username").setRequired(true));
		loginForm.add(new PasswordTextField("password").setRequired(true));
		loginForm.add(new CheckBox("rememberme"));
		loginForm.add(new Button("login", new ResourceModel("login")));

		add(loginForm);

		add(new BookmarkablePageLink<>("linkRegister", RegisterUserPage.class));

		/**
		 * Begin snippet Display users posts + posts of followed people, only if
		 * logged in
		 */

		IModel<List<Post>> postsModel = new LoadableDetachableModel<List<Post>>() {
			private static final long serialVersionUID = -1152204720704062151L;

			@Override
			protected List<Post> load() {
				if (JitterSession.get().isLoggedIn()) {
					return posts.getUserFeed(users.get(JitterSession.get()
							.getUserId()));
				} else {
					return posts.getAll();
				}
			}
		};

		add(new ListPostsPanel("postListing", postsModel));

		/**
		 * End snippet
		 */

		add(new BookmarkablePageLink<>("recoverPassLink",
				RecoverPasswordPage.class));

		// START POPULAR HASHTAGS
		class PopularHashtagsLoadableDetachableModel extends
				LoadableDetachableModel<List<RankedHashtag>> {
			private static final long serialVersionUID = 2902709917855929221L;
			// Days to consider
			private Integer days = 7;

			public Integer getDays() {
				return days;
			}

			public void setDays(Integer days) {
				if (days > 0 && days < 100)
					this.days = days;
			}

			@Override
			protected List<RankedHashtag> load() {
				return hashtags.getMostPopular(getDays());
			}

		}

		final PopularHashtagsLoadableDetachableModel hashtagsModel = new PopularHashtagsLoadableDetachableModel();
		ListHashtagsPanel listHashtagsPanel = new ListHashtagsPanel(
				"hashtagListing", hashtagsModel);
		add(listHashtagsPanel);

		class PopularHashtagsLink extends Link<Void> {
			private static final long serialVersionUID = 8203963721533098126L;
			private Integer days;

			public PopularHashtagsLink(String id, Integer days) {
				super(id);
				this.days = days;
			}

			@Override
			public void onClick() {
				hashtagsModel.setDays(days);
			}
		}

		add(new PopularHashtagsLink("popularHashtagsLink1day", 1));
		add(new PopularHashtagsLink("popularHashtagsLink7day", 7));
		add(new PopularHashtagsLink("popularHashtagsLink30day", 30));
		// END POPULAR HASHTAGS

		if (session.isLoggedIn()) {
			get("loginForm").setVisible(false);
			get("recoverPassLink").setVisible(false);
			get("linkRegister").setVisible(false);
		}
	}
}
