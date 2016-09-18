package ar.edu.itba.it.paw.domain.shortenedurl;

public interface ShortenedUrlRepo {

	public ShortenedUrl get(Integer id);

	public void add(ShortenedUrl shortenedUrl);

	public ShortenedUrl getByCode(String code);

}
