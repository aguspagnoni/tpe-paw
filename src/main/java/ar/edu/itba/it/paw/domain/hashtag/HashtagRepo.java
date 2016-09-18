package ar.edu.itba.it.paw.domain.hashtag;

import java.util.List;

public interface HashtagRepo {

	public Hashtag getByName(String hashtag);

	public void add(Hashtag hashtag);

	public List<RankedHashtag> getMostPopular(Integer days);
}
