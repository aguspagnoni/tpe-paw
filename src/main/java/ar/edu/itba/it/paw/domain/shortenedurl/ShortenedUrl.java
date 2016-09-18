package ar.edu.itba.it.paw.domain.shortenedurl;

import javax.persistence.Entity;

import ar.edu.itba.it.paw.domain.common.PersistentEntity;

@Entity
public class ShortenedUrl extends PersistentEntity {

	String code;
	String destination;

	ShortenedUrl() {
	}

	public ShortenedUrl(String code, String destination) {
		this.code = code;
		this.destination = destination;
	}

	public String getCode() {
		return code;
	}

	public String getDestination() {
		return destination;
	}
}
