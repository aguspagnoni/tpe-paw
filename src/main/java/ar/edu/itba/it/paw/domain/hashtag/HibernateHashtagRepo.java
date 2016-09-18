package ar.edu.itba.it.paw.domain.hashtag;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ar.edu.itba.it.paw.domain.common.AbstractHibernateRepo;

@Repository
public class HibernateHashtagRepo extends AbstractHibernateRepo implements
		HashtagRepo {

	@Autowired
	public HibernateHashtagRepo(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public Hashtag getByName(String name) {
		List<Hashtag> result = find(
				"FROM Hashtag hashtag WHERE hashtag.name = ?", name);
		return result.size() > 0 ? result.get(0) : null;
	}

	@Override
	public void add(Hashtag hashtag) {
		save(hashtag);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RankedHashtag> getMostPopular(Integer days) {
		Timestamp searchInterval = new Timestamp(java.util.Calendar
				.getInstance().getTimeInMillis() - TimeUnit.DAYS.toMillis(days));
		List<RankedHashtag> results = new LinkedList<RankedHashtag>();
		Session session = getSession();
		Query query = session
				.createQuery("SELECT count(posts) as popularity, hashtag FROM Hashtag hashtag JOIN hashtag.posts posts WHERE posts.created > ? GROUP BY hashtag.id ORDER BY popularity DESC");
		query.setParameter(0, searchInterval);
		query.setMaxResults(10);

		List<Object[]> objects = query.list();

		for (Object[] object : objects) {
			results.add(new RankedHashtag((Hashtag) object[1],
					((Long) object[0]).intValue()));
		}

		return results;
	}

	// @SuppressWarnings("unchecked")
	// @Override
	// public Map<Hashtag, Integer> getMostPopular(Integer days) {
	// Timestamp searchInterval = new Timestamp(java.util.Calendar.getInstance()
	// .getTimeInMillis() - TimeUnit.DAYS.toMillis(days));
	// Map<Hashtag, Integer> results = new LinkedHashMap<Hashtag, Integer>();
	// Session session = getSession();
	// Query query = session
	// .createQuery("SELECT count(posts) as popularity, hashtag FROM Hashtag hashtag JOIN hashtag.posts posts WHERE posts.created > ? GROUP BY hashtag.id ORDER BY popularity DESC");
	// query.setParameter(0, searchInterval);
	// query.setMaxResults(10);
	//
	// List<Object[]> objects = query.list();
	//
	// for (Object[] object : objects) {
	// results.put((Hashtag) object[1], ((Long) object[0]).intValue());
	// }
	//
	// return results;
	// }
}
