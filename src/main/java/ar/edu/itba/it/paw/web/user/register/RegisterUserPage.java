package ar.edu.itba.it.paw.web.user.register;

import org.apache.wicket.extensions.markup.html.captcha.CaptchaImageResource;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.value.ValueMap;
import org.apache.wicket.validation.validator.StringValidator;

import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepo;
import ar.edu.itba.it.paw.exception.DuplicatedUserException;
import ar.edu.itba.it.paw.exception.InvalidPasswordException;
import ar.edu.itba.it.paw.exception.InvalidUsernameException;
import ar.edu.itba.it.paw.web.JitterSession;
import ar.edu.itba.it.paw.web.index.BasePage;
import ar.edu.itba.it.paw.web.index.IndexPage;

public class RegisterUserPage extends BasePage {
	private static final long serialVersionUID = -8819660521092107364L;

	@SpringBean
	private UserRepo users;

	private CaptchaImageResource captchaImageResource;
	private String imagePass;
	private final ValueMap properties = new ValueMap();

	private transient String firstname;
	private transient String lastname;
	private transient String username;
	private transient String password;
	@SuppressWarnings("unused")
	private transient String repassword;
	private transient String description;
	private transient String question;
	private transient String answer;
	private transient Boolean viewable;

	public RegisterUserPage() {

		add(new FeedbackPanel("feedback"));

		Form<RegisterUserPage> registerForm = new Form<RegisterUserPage>(
				"registerForm", new CompoundPropertyModel<RegisterUserPage>(
						this));
		imagePass = randomString(6, 8);
		captchaImageResource = new CaptchaImageResource(imagePass);
		final NonCachingImage captchaImage = new NonCachingImage(
				"captchaImage", captchaImageResource);
		registerForm.add(captchaImage);
		registerForm.add(new RequiredTextField<String>("captcha",
				new PropertyModel<String>(properties, "captcha")) {
			private static final long serialVersionUID = 5434026789614097885L;

			@Override
			protected final void onComponentTag(final ComponentTag tag) {
				super.onComponentTag(tag);
				// clear the field after each render
				tag.put("value", "");
			}
		});

		registerForm.add(new TextField<String>("firstname").setRequired(true));
		registerForm.add(new TextField<String>("lastname").setRequired(true));
		registerForm.add(new TextField<String>("username").setRequired(true));
		registerForm.add(new PasswordTextField("password").setRequired(true)
				.add(StringValidator.minimumLength(6)));
		registerForm.add(new PasswordTextField("repassword").setRequired(true)
				.add(StringValidator.minimumLength(6)));
		registerForm.add(new TextField<String>("description"));
		registerForm.add(new TextField<String>("question").setRequired(true));
		registerForm.add(new TextField<String>("answer").setRequired(true));
		registerForm.add(new CheckBox("viewable"));

		registerForm.add(new Button("submit") {
			private static final long serialVersionUID = 3330932044547248397L;

			@Override
			public void onSubmit() {
				try {
					if (!imagePass.equals(getCaptcha())) {
						error("Captcha '" + getCaptcha() + "' is wrong.\n"
								+ "Correct captcha was: " + imagePass);
						captchaImageResource.invalidate();
						imagePass = randomString(6, 8);
						captchaImageResource = new CaptchaImageResource(
								imagePass);
						captchaImage.setImageResource(captchaImageResource);
					} else {
						User user = new User(username, password, firstname,
								lastname, question, answer, description, 0, 0,
								viewable);
						users.add(user);
						JitterSession session = JitterSession.get();
						session.bind();
						session.logIn(username, password, users);
						info("Success!");
						setResponsePage(new IndexPage());
					}
				} catch (DuplicatedUserException e) {
					error("This username is not available");
				} catch (InvalidUsernameException e) {
					error("Username is invalid, must be A-z only, no spaces.");
				} catch (InvalidPasswordException e) {
					error("Password is invalid, must be A-z0-9 only, no spaces");
				}
			}
		});
		add(registerForm);
	}

	private static int randomInt(int min, int max) {
		return (int) (Math.random() * (max - min) + min);
	}

	private static String randomString(int min, int max) {
		int num = randomInt(min, max);
		byte b[] = new byte[num];
		for (int i = 0; i < num; i++)
			b[i] = (byte) randomInt('a', 'z');
		return new String(b);
	}

	private String getCaptcha() {
		return properties.getString("captcha");
	}
}
