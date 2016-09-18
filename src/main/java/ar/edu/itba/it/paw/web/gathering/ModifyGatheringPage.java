package ar.edu.itba.it.paw.web.gathering;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import ar.edu.itba.it.paw.domain.common.EntityModel;
import ar.edu.itba.it.paw.domain.gathering.Gathering;
import ar.edu.itba.it.paw.domain.user.UserRepo;
import ar.edu.itba.it.paw.web.index.BasePage;

public class ModifyGatheringPage extends BasePage {
	private static final long serialVersionUID = -1825423604392437824L;

	@SpringBean
	private UserRepo users;
	@SuppressWarnings("unused")
	private String name;

	public ModifyGatheringPage(Gathering gathering) {

		Form<Gathering> form = new Form<Gathering>("form",
				new CompoundPropertyModel<Gathering>(
						new EntityModel<Gathering>(Gathering.class, gathering)));

		form.add(new TextField<String>("name").setRequired(true).add(
				new StringValidator(1, 100)));
		form.add(new Button("submit") {
			private static final long serialVersionUID = 4771892069353943335L;

			@Override
			public void onSubmit() {
				setResponsePage(ListGatheringsPage.class);
			}
		});
		add(form);
	}
}
