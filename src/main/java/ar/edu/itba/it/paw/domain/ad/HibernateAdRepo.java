package ar.edu.itba.it.paw.domain.ad;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.it.paw.domain.common.AbstractHibernateRepo;

@Repository
public class HibernateAdRepo extends AbstractHibernateRepo implements AdRepo {

	@Autowired
	public HibernateAdRepo(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public Ad getRandom() {
		Session session = getSession();
		Query query = session.createQuery("FROM Ad ORDER BY rand()");
		query.setMaxResults(1);
		@SuppressWarnings("unchecked")
		List<Ad> result = query.list();
		return result.size() > 0 ? result.get(0) : null;
	}
}
