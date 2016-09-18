package ar.edu.itba.it.paw.web.ad;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.ad.Ad;
import ar.edu.itba.it.paw.domain.ad.AdRepo;

public class AdPanel extends Panel {
	private static final long serialVersionUID = 2901926796589700578L;
	@SpringBean
	private AdRepo ads;

	public AdPanel(String id) {
		super(id);

		IModel<Ad> model = new LoadableDetachableModel<Ad>() {
			private static final long serialVersionUID = -2850878665622081762L;

			@Override
			protected Ad load() {
				return ads.getRandom();
			}
		};

		ExternalLink adLink = new ExternalLink("adLink",
				new PropertyModel<String>(model, "linkUrl"));
		Image adImage = new Image("imageUrl", "");
		adImage.add(new AttributeModifier("src", new PropertyModel<String>(
				model, "imageUrl")));
		adLink.add(adImage);
		add(adLink);
	}
}
