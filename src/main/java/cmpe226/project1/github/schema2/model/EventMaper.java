package cmpe226.project1.github.schema2.model;

import cmpe226.project1.github.schema2.model.ActorSingle;
import cmpe226.project1.github.schema2.model.RepositorySingle;

import com.google.gson.annotations.SerializedName;

public class EventMaper {
	

	private String url;
	private String type;
	private String created_at;

	@SerializedName("public")
	private boolean is_public;

	
	private RepositorySingle repository;

	
	@SerializedName("actor_attributes")
	private ActorSingle actor;

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		// sb.append("id:'"+ id +"'");
		sb.append("url:'" + url + "'");
		sb.append(",type:'" + type + "'");
		sb.append(",created_at:'" + created_at + "'");
		if (repository != null)
			sb.append(",repository:" + repository.toString());
		if (actor != null)
			sb.append(",actor_attributes:" + actor.toString());
		sb.append("}");
		return sb.toString();
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

	public RepositorySingle getRepository() {
		return repository;
	}

	public void setRepository(RepositorySingle repository) {
		this.repository = repository;
	}

	public ActorSingle getActor() {
		return actor;
	}

	public void setActor(ActorSingle actor) {
		this.actor = actor;
	}

}
