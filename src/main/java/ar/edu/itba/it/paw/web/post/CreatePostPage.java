package ar.edu.itba.it.paw.web.post;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import ar.edu.itba.it.paw.domain.post.Post;
import ar.edu.itba.it.paw.domain.post.PostRepo;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepo;
import ar.edu.itba.it.paw.web.JitterSession;
import ar.edu.itba.it.paw.web.index.BasePage;
import ar.edu.itba.it.paw.web.index.IndexPage;
import ar.edu.itba.it.paw.web.user.profile.ProfilePage;

public class CreatePostPage extends BasePage {
	private static final long serialVersionUID = 2340884016122981067L;
	@SpringBean
	private UserRepo users;
	@SpringBean
	private PostRepo posts;

	private transient String body;

	public CreatePostPage() {
		super();

		add(new FeedbackPanel("feedback"));

		Form<CreatePostPage> createPostForm = new Form<CreatePostPage>(
				"createPostForm", new CompoundPropertyModel<CreatePostPage>(
						this));

		createPostForm.add(new TextArea<String>("body")
				.add(new StringValidator(0, 140)));
		createPostForm.add(new Button("submit") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit() {
				JitterSession jitterSession = JitterSession.get();
				Integer userId = jitterSession.getUserId();

				if (userId == null) {
					setResponsePage(IndexPage.class);
				} else {
					User loggedInUser = users.get(userId);
					Post post = new Post(body);
					posts.add(loggedInUser, post);
					setResponsePage(
							ProfilePage.class,
							new PageParameters().set("username",
									loggedInUser.getUsername()));
				}
			}

		});

		add(createPostForm);
	}
}
