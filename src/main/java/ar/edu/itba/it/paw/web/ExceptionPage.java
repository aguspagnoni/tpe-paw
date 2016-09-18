package ar.edu.itba.it.paw.web;

import org.apache.log4j.Logger;
import org.apache.wicket.markup.html.WebPage;

public class ExceptionPage extends WebPage {

	private static final long serialVersionUID = 593717625018028083L;
	private transient final Logger logger = Logger.getLogger(this.getClass());

	public ExceptionPage(Exception e) {
		// email on error
		logger.error("ERROR: ", e);
	}
}