package ar.edu.itba.it.paw.domain.hashtag;

public class RankedHashtag {
	private Hashtag hashtag;
	private Integer rank;

	public Hashtag getHashtag() {
		return hashtag;
	}

	public void setHashtag(Hashtag hashtag) {
		this.hashtag = hashtag;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public RankedHashtag(Hashtag hashtag, Integer rank) {
		super();
		this.hashtag = hashtag;
		this.rank = rank;
	}

}
