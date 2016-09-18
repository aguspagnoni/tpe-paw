package ar.edu.itba.it.paw.web.command;

import ar.edu.itba.it.paw.domain.user.User;

public class RecoverPasswordForm {

	private int id;
	private String username;
	private String password;
	private String repassword;
	private String question;
	private String answer;

	public RecoverPasswordForm() {
	}

	public RecoverPasswordForm(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.question = user.getQuestion();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRepassword() {
		return repassword;
	}

	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}
}
