package ar.edu.itba.it.paw.web.user.recoverPassword;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepo;
import ar.edu.itba.it.paw.web.index.BasePage;

public class RecoverPasswordPage extends BasePage {
	private static final long serialVersionUID = -3017997799198236774L;
	@SpringBean
	UserRepo users;
	private transient String username;

	public RecoverPasswordPage() {

		final IModel<String> usernameModel = new IModel<String>() {
			private static final long serialVersionUID = -6200883466738753120L;

			@Override
			public String getObject() {
				return username;
			}

			@Override
			public void setObject(String object) {
				username = object;
			}

			@Override
			public void detach() {
			}
		};

		Form<RecoverPasswordPage> recoverForm = new Form<RecoverPasswordPage>(
				"recoverForm");
		recoverForm.add(new TextField<String>("username", usernameModel)
				.setRequired(true));

		recoverForm.add(new Button("continue") {
			private static final long serialVersionUID = -5163570821882662442L;

			@Override
			public void onSubmit() {
				super.onSubmit();
				User user = users.getByName(username);
				if (user != null) {
					setResponsePage(new RecoverPasswordNext(user));
				} else {
					error(getString("UsernameNotFound"));
				}
			}
		});

		add(new FeedbackPanel("recoverFeedback"));
		add(recoverForm);
	}
}
