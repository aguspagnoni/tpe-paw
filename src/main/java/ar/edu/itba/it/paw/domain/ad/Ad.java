package ar.edu.itba.it.paw.domain.ad;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.edu.itba.it.paw.domain.common.PersistentEntity;

@Entity
@Table(name = "ads")
public class Ad extends PersistentEntity {
	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String imageUrl;

	@Column(nullable = false)
	private String linkUrl;
}
