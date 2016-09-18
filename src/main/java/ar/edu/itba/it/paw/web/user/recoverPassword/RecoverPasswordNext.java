package ar.edu.itba.it.paw.web.user.recoverPassword;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepo;
import ar.edu.itba.it.paw.web.JitterSession;
import ar.edu.itba.it.paw.web.index.BasePage;
import ar.edu.itba.it.paw.web.user.profile.EditProfilePage;

public class RecoverPasswordNext extends BasePage {
	private static final long serialVersionUID = 8160563495704283035L;
	@SpringBean
	UserRepo users;
	private transient String typedanswer;
	private transient String typedpassword;
	JitterSession session = JitterSession.get();

	public RecoverPasswordNext(User user) {

		final String answer = user.getAnswer();
		final String username = user.getUsername();

		final IModel<String> answerModel = new IModel<String>() {
			private static final long serialVersionUID = -3310390250350317277L;

			@Override
			public String getObject() {
				return typedanswer;
			}

			@Override
			public void setObject(String object) {
				typedanswer = object;
			}

			@Override
			public void detach() {
			}
		};

		final IModel<String> passwordModel = new IModel<String>() {
			private static final long serialVersionUID = -3310390250350317277L;

			@Override
			public String getObject() {
				return typedpassword;
			}

			@Override
			public void setObject(String object) {
				typedpassword = object;
			}

			@Override
			public void detach() {
			}
		};

		Form<RecoverPasswordPage> recoverForm = new Form<RecoverPasswordPage>(
				"recoverForm");

		recoverForm.add(new Label("username", username));
		recoverForm.add(new Label("question", user.getQuestion()));
		recoverForm.add(new TextField<String>("answer", answerModel)
				.setRequired(true));

		PasswordTextField passwordField = new PasswordTextField("password",
				passwordModel);
		passwordField.setRequired(true);
		passwordField.setResetPassword(false);
		passwordField.add(StringValidator.minimumLength(6));
		// passwordField.add(new PasswordValidator());
		recoverForm.add(passwordField);

		PasswordTextField confirmPasswordField = new PasswordTextField(
				"confirmPassword", passwordModel);
		confirmPasswordField.setRequired(true);
		confirmPasswordField.setResetPassword(false);
		confirmPasswordField.add(StringValidator.minimumLength(6));
		// confirmPasswordField.add(new PasswordValidator());
		recoverForm.add(confirmPasswordField);

		recoverForm.add(new EqualPasswordInputValidator(passwordField,
				confirmPasswordField));

		recoverForm.add(new Button("recover") {
			private static final long serialVersionUID = -4780499064587252712L;

			@Override
			public void onSubmit() {
				super.onSubmit();
				if (answer.equals(typedanswer)) {
					users.getByName(username).setPassword(typedpassword);
					session.logIn(username, typedpassword, users);
					setResponsePage(new EditProfilePage(users
							.getByName(username)));
				} else {
					error(getString("wronganswer"));
				}
			}
		});

		add(new FeedbackPanel("recoverFeedback"));
		add(recoverForm);
	}
}
