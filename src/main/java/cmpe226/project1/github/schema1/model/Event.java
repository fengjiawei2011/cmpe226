package cmpe226.project1.github.schema1.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.google.gson.annotations.SerializedName;

@Entity
@Table(name = "event")
public class Event {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "event_id", columnDefinition = "serial", updatable = false, nullable = false)
	private Long id;
	
	@Column(length = 1024)
	private String url;
	private String type;
	private String created_at;

	@SerializedName("public")
	private boolean is_public;

	@ManyToOne
	@JoinColumn(name="repo_id")
	private Repository repository;

	@ManyToOne
	@JoinColumn(name="actor_id")
	@SerializedName("actor_attributes")
	private Actor actor;

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		// sb.append("id:'"+ id +"'");
		sb.append("url:'" + url + "'");
		sb.append(",type:'" + type + "'");
		sb.append(",created_at:'" + created_at + "'");
		if(repository != null)
			sb.append(",repository:" + repository.toString());
		if(actor != null)
			sb.append(",actor_attributes:" + actor.toString());
		sb.append("}");
		return sb.toString();
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public boolean isIs_public() {
		return is_public;
	}

	public void setIs_public(boolean is_public) {
		this.is_public = is_public;
	}

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	public Actor getActor() {
		return actor;
	}

	public void setActor(Actor actor) {
		this.actor = actor;
	}
	
	
}
