package ar.edu.itba.it.paw.web.gathering;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.gathering.Gathering;
import ar.edu.itba.it.paw.domain.user.User;
import ar.edu.itba.it.paw.domain.user.UserRepo;
import ar.edu.itba.it.paw.web.JitterSession;
import ar.edu.itba.it.paw.web.index.BasePage;
import ar.edu.itba.it.paw.web.index.IndexPage;

import org.apache.wicket.validation.validator.StringValidator;

public class CreateGatheringPage extends BasePage {
	private static final long serialVersionUID = 2329331589874164726L;

	@SpringBean
	private UserRepo users;

	private String name;

	public CreateGatheringPage() {

		/**
		 * Create new gathering below
		 */

		Form<CreateGatheringPage> form = new Form<CreateGatheringPage>("form",
				new CompoundPropertyModel<CreateGatheringPage>(this));

		form.add(new TextField<String>("name").setRequired(true).add(
				new StringValidator(1, 100)));
		form.add(new Button("submit") {
			private static final long serialVersionUID = -5362698056418095484L;

			@Override
			public void onSubmit() {
				JitterSession jitterSession = JitterSession.get();
				Integer userId = jitterSession.getUserId();

				if (userId == null) {
					setResponsePage(IndexPage.class);
				} else {
					// Create new gathering
					User loggedInUser = users.get(userId);
					Gathering gathering = new Gathering(name, loggedInUser);
					loggedInUser.createGathering(gathering);
					setResponsePage(ListGatheringsPage.class);
				}
			}
		});
		add(form);
	}
}
