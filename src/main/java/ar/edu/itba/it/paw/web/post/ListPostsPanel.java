package ar.edu.itba.it.paw.web.post;

import java.util.List;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.ocpsoft.prettytime.PrettyTime;

import ar.edu.itba.it.paw.domain.notification.Notification;
import ar.edu.itba.it.paw.domain.notification.Notification.Type;
import ar.edu.itba.it.paw.domain.post.Post;
import ar.edu.itba.it.paw.domain.post.PostRepo;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepo;
import ar.edu.itba.it.paw.web.JitterApplication;
import ar.edu.itba.it.paw.web.JitterSession;
import ar.edu.itba.it.paw.web.index.IndexPage;
import ar.edu.itba.it.paw.web.user.profile.ProfilePage;

public class ListPostsPanel extends Panel {

	@SpringBean
	UserRepo users;
	@SpringBean
	PostRepo posts;

	private static final String baseUrl = "/jitter/bin";

	private static final long serialVersionUID = -7683832554000111674L;

	public ListPostsPanel(String id, IModel<List<Post>> postsModel) {
		super(id, postsModel);

		ListView<Post> listview = new PropertyListView<Post>("post", postsModel) {
			private static final long serialVersionUID = -5251472329256050213L;

			@Override
			protected void populateItem(ListItem<Post> item) {

				// Link to original author, if reposted post
				item.add(new BookmarkablePageLink<Post>("authorLink",
						ProfilePage.class, new PageParameters().set("username",
								item.getModelObject().getUser().getUsername()))
						.add(new Label("user.username")));

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

				postBodyModel.setObject(item.getModelObject().getBody());

				item.add(new Label("body", postBodyModel)
						.setEscapeModelStrings(false));
				PrettyTime p = new PrettyTime();
				item.add(new Label("created", p.format(item.getModelObject()
						.getCreated())));

				// Link to original author, if reposted post
				item.add((new Link<Post>("repostAuthorLink", item.getModel()) {
					private static final long serialVersionUID = -5455449449197702152L;

					@Override
					public void onClick() {
						setResponsePage(ProfilePage.class, new PageParameters()
								.set("username", getModelObject()
										.getOriginalPost().getUser()
										.getUsername()));
					}
				}).add(new Label("originalPost.user.username")));

				item.add(new Image("distinguished", JitterApplication.STAR_ICON));
				
				if (item.getModelObject().isRePost()
						&& !item.getModelObject().getOriginalPost().getUser()
								.isDistinguished()) {
					item.get("distinguished").setVisible(false);
				}

				item.add((new Link<Post>("repostPostLink", item.getModel()) {
					private static final long serialVersionUID = -127233257550871468L;

					@Override
					public void onClick() {
						Post post = getModelObject();
						Integer userId = JitterSession.get().getUserId();
						if (userId != null) {
							User loggedInUser = users.get(userId);
							Post repost = post.getRepost();
							Notification notification = new Notification(repost
									.getBody(), loggedInUser, Type.REPOST);
							notification.notify(post.getUser());
							posts.add(loggedInUser, repost);
							setResponsePage(IndexPage.class);
						}
					}

					@Override
					protected void onConfigure() {
						Integer userId = JitterSession.get().getUserId();
						if (userId == null) {
							setVisible(false);
						}
						super.onConfigure();
					}

				}));

				item.add((new Link<Post>("favoritePostLink", item.getModel()) {
					private static final long serialVersionUID = 6754416364305450892L;

					@Override
					public void onClick() {
						Post post = getModelObject();
						Integer userId = JitterSession.get().getUserId();
						if (userId != null) {
							User loggedInUser = users.get(userId);
							loggedInUser.favorite(post);
						}
					}

					@Override
					protected void onConfigure() {
						Integer userId = JitterSession.get().getUserId();
						if (userId == null || !JitterSession.get().isLoggedIn()) {
							setVisible(false);
						} else {
							User loggedInUser = users.get(userId);
							if (loggedInUser.getFavoritePosts().contains(
									getModelObject())) {
								setVisible(false);
							}
						}
						super.onConfigure();
					}

				}));

				item.add((new Link<Post>("unfavoritePostLink", item.getModel()) {

					private static final long serialVersionUID = 5748126979028493509L;

					@Override
					public void onClick() {
						Post post = getModelObject();
						Integer userId = JitterSession.get().getUserId();
						if (userId != null) {
							User loggedInUser = users.get(userId);
							loggedInUser.unfavorite(post);
						}
					}

					@Override
					protected void onConfigure() {
						Integer userId = JitterSession.get().getUserId();

						// If user is not logged in or hasn't already
						// favorited the post, hide link
						if (userId == null) {
							setVisible(false);
						} else {
							User loggedInUser = users.get(userId);
							if (!loggedInUser.getFavoritePosts().contains(
									getModelObject())) {
								setVisible(false);
							}
						}
						super.onConfigure();
					}

				}));

				item.add((new Link<Post>("deletePostLink", item.getModel()) {
					private static final long serialVersionUID = -2519679515753280741L;

					@Override
					public void onClick() {
						Post post = getModelObject();
						Integer userId = JitterSession.get().getUserId();
						if (userId != null) {
							if (userId == post.getUser().getId()) {
								User user = users.get(userId);
								user.deletePost(post);
								setResponsePage(IndexPage.class);
							} else {
								setResponsePage(IndexPage.class);
							}
						}
					}

					@Override
					protected void onConfigure() {
						Integer userId = JitterSession.get().getUserId();
						if (userId == null
								|| userId != getModelObject().getUser().getId()) {
							setVisible(false);
						}
						super.onConfigure();
					}
				}));

				if (!item.getModelObject().getUser().isDistinguished()) {
					item.get("distinguished").setVisible(false);
				}
			}
		};
		add(listview);
	}
}
