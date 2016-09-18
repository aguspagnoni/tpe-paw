package ar.edu.itba.it.paw.web.common;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

import ar.edu.itba.it.paw.web.JitterApplication;

public abstract class DeleteLink<T> extends Link<T> {

	private static final long serialVersionUID = 7043316760557324439L;

	public DeleteLink(String id, IModel<T> model) {
		super(id, model);
		add(new Image("imgRemove", JitterApplication.DELETE_ICON));
		add(new AttributeModifier(
				"onclick",
				"return confirm('Are you sure you want to remove this user from your blacklist?');"));
	}
}
