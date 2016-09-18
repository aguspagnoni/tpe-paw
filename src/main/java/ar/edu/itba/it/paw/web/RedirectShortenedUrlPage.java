package ar.edu.itba.it.paw.web;

import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.shortenedurl.ShortenedUrl;
import ar.edu.itba.it.paw.domain.shortenedurl.ShortenedUrlRepo;
import ar.edu.itba.it.paw.web.index.BasePage;

public class RedirectShortenedUrlPage extends BasePage {
	private static final long serialVersionUID = -4969550902655564466L;
	@SpringBean
	private ShortenedUrlRepo shortenedUrls;

	public RedirectShortenedUrlPage(final PageParameters parameters) {
		super();
		String urlCode = parameters.get("urlCode").toString();
		final ShortenedUrl shortenedUrl = shortenedUrls.getByCode(urlCode);
		setResponsePage(new RedirectPage(shortenedUrl.getDestination()));
	}
}
