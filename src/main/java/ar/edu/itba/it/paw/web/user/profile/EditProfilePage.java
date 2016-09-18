package ar.edu.itba.it.paw.web.user.profile;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import ar.edu.itba.it.paw.domain.common.EntityModel;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepo;
import ar.edu.itba.it.paw.web.JitterSession;
import ar.edu.itba.it.paw.web.index.BasePage;

public class EditProfilePage extends BasePage {
	private static final long serialVersionUID = -6651293952435169985L;

	@SpringBean
	UserRepo users;

	static JitterSession session = JitterSession.get();

	public EditProfilePage(User user) {
		add(new FeedbackPanel("errorPanel"));
		Form<User> form = new Form<User>("editProfileForm",
				new CompoundPropertyModel<User>(new EntityModel<User>(
						User.class, user)));

		form.add(new Button("submit", new ResourceModel("submit")) {
			private static final long serialVersionUID = -5363820808621952837L;

			@Override
			public void onSubmit() {
				info("Your profile was updated.");
			}
		});

		TextField<String> usernameField = new TextField<String>("username");
		usernameField.setRequired(true);
		usernameField.setEnabled(false);
		// usernameField.add(new NameValidator());
		form.add(usernameField);

		TextField<String> firstNameField = new TextField<String>("firstName");
		firstNameField.setRequired(true);
		// firstNameField.add(new FirstNameValidator());
		form.add(firstNameField);

		TextField<String> lastNameField = new TextField<String>("lastName");
		lastNameField.setRequired(true);
		// lastNameField.add(new LastNameValidator());
		form.add(lastNameField);

		TextField<String> descriptionField = new TextField<String>(
				"description");
		descriptionField.setRequired(true);
		form.add(descriptionField);

		TextField<String> questionField = new TextField<String>("question");
		questionField.setRequired(true);
		form.add(questionField);

		TextField<String> answerField = new TextField<String>("answer");
		answerField.setRequired(true);
		form.add(answerField);

		PasswordTextField passwordField = new PasswordTextField("password");
		passwordField.setRequired(true);
		passwordField.setResetPassword(false);
		passwordField.add(StringValidator.minimumLength(6));
		// passwordField.add(new PasswordValidator());
		form.add(passwordField);

		PasswordTextField confirmPasswordField = new PasswordTextField(
				"confirmPassword");
		confirmPasswordField.setRequired(true);
		confirmPasswordField.setResetPassword(false);
		confirmPasswordField.add(StringValidator.minimumLength(6));
		// confirmPasswordField.add(new PasswordValidator());
		form.add(confirmPasswordField);

		form.add(new CheckBox("viewable"));
		form.add(new EqualPasswordInputValidator(passwordField,
				confirmPasswordField));
		add(form);
	}
}
