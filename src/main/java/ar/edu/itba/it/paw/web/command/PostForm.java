package ar.edu.itba.it.paw.web.command;

import ar.edu.itba.it.paw.domain.post.Post;

public class PostForm {

	private String body;

	public PostForm() {
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Post build() {
		return new Post(body);
	}

}
