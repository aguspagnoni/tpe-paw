package ar.edu.itba.it.paw.domain.shortenedurl;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.it.paw.domain.common.AbstractHibernateRepo;

@Repository
public class HibernateShortenedUrlRepo extends AbstractHibernateRepo implements
		ShortenedUrlRepo {

	@Autowired
	public HibernateShortenedUrlRepo(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public ShortenedUrl get(Integer id) {
		return get(ShortenedUrl.class, id);
	}

	@Override
	public void add(ShortenedUrl shortenedUrl) {
		save(shortenedUrl);
	}

	@Override
	public ShortenedUrl getByCode(String code) {
		List<ShortenedUrl> result = find("FROM ShortenedUrl WHERE code = ?",
				code);
		return result.size() > 0 ? result.get(0) : null;
	}
}
