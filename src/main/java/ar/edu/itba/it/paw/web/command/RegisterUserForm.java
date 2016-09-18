package ar.edu.itba.it.paw.web.command;

import ar.edu.itba.it.paw.domain.user.User;

public class RegisterUserForm {

	private int id;
	private String firstname;
	private String lastname;
	private String username;
	private String password;
	private String repassword;
	private String description;
	private String question;
	private String answer;
	private Boolean viewable;

	public RegisterUserForm() {
	}

	public RegisterUserForm(User user) {
		this.id = user.getId();
		this.firstname = user.getFirstName();
		this.lastname = user.getLastName();
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.repassword = user.getPassword();
		this.description = user.getDescription();
		this.question = user.getQuestion();
		this.answer = user.getAnswer();
		this.viewable = user.getViewable();
	}

	public boolean isNewMode() {
		return id == 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Boolean getViewable() {
		return viewable;
	}

	public void setViewable(Boolean viewable) {
		this.viewable = viewable;
	}

	public User build() {
		return new User(username, password, firstname, lastname, question,
				answer, description, 0, 0, viewable);
	}

	public void update(User user) {
		user.setFirstName(firstname);
		user.setLastName(lastname);
		user.setPassword(password);
		user.setAnswer(answer);
		user.setQuestion(question);
		user.setDescription(description);
		user.setViewable(viewable);
	}
}
