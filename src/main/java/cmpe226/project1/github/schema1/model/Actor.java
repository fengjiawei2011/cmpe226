package cmpe226.project1.github.schema1.model;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="actor")
public class Actor {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "actor_id", columnDefinition = "serial", updatable = false, nullable = false)
	private Long id;
	
	private String gravatar_id;
	
	@Column(unique = true, updatable = false, nullable = false)
	private String login = "";
	
	private String type = "";
	private String name = "";
	private String blog = "";
	private String location = "";
	private String email = "";
	private String company = "";

	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("gravatar_id:'"+ gravatar_id+"'");
		sb.append(",login:'"+login+"'");
		sb.append(",name:'"+name+"'");
		sb.append(",blog:'"+blog+"'");
		sb.append(",location:'"+location+"'");
		sb.append(",email:'"+email+"'");
		sb.append(",company:'"+company+"'");
		sb.append("}");
		return sb.toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getGravatar_id() {
		return gravatar_id;
	}

	public void setGravatar_id(String gravatar_id) {
		this.gravatar_id = gravatar_id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBlog() {
		return blog;
	}

	public void setBlog(String blog) {
		this.blog = blog;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}
	
}
	