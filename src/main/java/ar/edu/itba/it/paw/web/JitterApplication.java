package ar.edu.itba.it.paw.web;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.markup.html.SecurePackageResourceGuard;
import org.apache.wicket.markup.html.pages.ExceptionErrorPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ar.edu.itba.it.paw.web.common.HibernateRequestCycleListener;
import ar.edu.itba.it.paw.web.gathering.ViewGatheringPage;
import ar.edu.itba.it.paw.web.hashtag.ViewHashtagPage;
import ar.edu.itba.it.paw.web.index.IndexPage;
import ar.edu.itba.it.paw.web.user.profile.ProfilePage;

@Component
public class JitterApplication extends WebApplication {

	private class JitterSecurePackageResourceGuard extends
			SecurePackageResourceGuard {
		public JitterSecurePackageResourceGuard() {
			super();
			addPattern("+*.min.map");
		}
	}

	public static final ResourceReference ADD_ICON = new PackageResourceReference(
			JitterApplication.class, "resources/add.png");
	public static final ResourceReference EDIT_ICON = new PackageResourceReference(
			JitterApplication.class, "resources/edit.gif");
	public static final ResourceReference DELETE_ICON = new PackageResourceReference(
			JitterApplication.class, "resources/delete.gif");
	public static final ResourceReference STAR_ICON = new PackageResourceReference(
			JitterApplication.class, "resources/star.png");
	public static Integer distinguished;
	private final SessionFactory sessionFactory;

	@Autowired
	public JitterApplication(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return IndexPage.class;
	}

	@Override
	public Session newSession(Request request, Response response) {
		return new JitterSession(request);
	}

	@Override
	protected void init() {
		super.init();
		try {
			Properties prop = new Properties();
			InputStream input = new FileInputStream("config.properties");
			prop.load(input);
			distinguished = new Integer(prop.getProperty("distinguished"));
		} catch (IOException e) {
		}
		getComponentInstantiationListeners().add(
				new SpringComponentInjector(this));
		getRequestCycleListeners().add(
				new HibernateRequestCycleListener(sessionFactory));

		mountPage("/profile/${username}", ProfilePage.class);
		mountPage("/hashtag/${hashtagName}", ViewHashtagPage.class);
		mountPage("/s/v/${urlCode}", RedirectShortenedUrlPage.class);
		mountPage("/gatherings/${id}", ViewGatheringPage.class);
		getRequestCycleListeners().add(new AbstractRequestCycleListener() {
			@Override
			public IRequestHandler onException(RequestCycle cycle, Exception e) {
				return new RenderPageRequestHandler(new PageProvider(
						new ExceptionErrorPage(e, new ExceptionPage(e))));
			}
		});

		// To fix warnings
		getResourceSettings().setPackageResourceGuard(
				new JitterSecurePackageResourceGuard());

	}
}
