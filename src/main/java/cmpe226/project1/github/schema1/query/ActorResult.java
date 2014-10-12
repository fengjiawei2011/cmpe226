package cmpe226.project1.github.schema1.query;

public class ActorResult {
	private Long actor_id; 
	private String login;
	private String email;
	private Long number_of_events;
	
	public ActorResult(Long actor_id, String login, String email,
			Long number_of_events) {
		super();
		this.actor_id = actor_id;
		this.login = login;
		this.email = email;
		this.number_of_events = number_of_events;
	}


	@Override
	public String toString() {
		return "ActorResult [actor_id=" + actor_id + ", login=" + login
				+ ", email=" + email + ", number_of_events=" + number_of_events
				+ "]";
	}


	public Long getActor_id() {
		return actor_id;
	}

	public void setActor_id(Long actor_id) {
		this.actor_id = actor_id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getNumber_of_events() {
		return number_of_events;
	}

	public void setNumber_of_events(Long number_of_events) {
		this.number_of_events = number_of_events;
	}
	
	

}
