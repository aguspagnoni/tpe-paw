package ar.edu.itba.it.paw.web.index;

import javax.servlet.http.Cookie;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.spring.injection.annot.SpringBean;

import ar.edu.itba.it.paw.domain.user.UserRepo;
import ar.edu.itba.it.paw.web.JitterSession;
import ar.edu.itba.it.paw.web.ad.AdPanel;

public class BasePage extends WebPage {

	private static final long serialVersionUID = 113191901088272322L;

	@SpringBean
	private UserRepo users;

	public BasePage() {
		super();

		// Check if user has a token saved in cookie and is not logged in, if
		// so, log in
		Cookie usernameCookie = ((WebRequest) getRequestCycle().getRequest())
				.getCookie("username");
		Cookie tokenCookie = ((WebRequest) getRequestCycle().getRequest())
				.getCookie("token");

		if ((!JitterSession.exists() | !JitterSession.get().isLoggedIn())
				&& usernameCookie != null && tokenCookie != null) {
			// Check token. If correct, try to log in user
			JitterSession.get().logInByToken(usernameCookie.getValue(),
					tokenCookie.getValue(), users);
		}

		add(new NavigationPanel("navigationPanel"));
		add(new AdPanel("adPanel"));
	}

}
