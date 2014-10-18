package cmpe226.project1.github.schema2.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.transaction.Transactional.TxType;


@Entity
@Table(name = "event_single_table")
public class EventSingle {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "event_id", updatable = false, nullable = false)
	private Long event_id;
	
	@Column(length = 1024)
	private String event_url;
	private String event_type;
	private String event_created_at;

	//@SerializedName("public")
	private boolean event_is_public;

	// ********actor*********

	private String gravatar_id;

	//@Column(unique = true, updatable = false, nullable = false)
	private String actor_login = "";
	private String actor_type = "";
	private String actor_name = "";
	@Column(columnDefinition = "text")
	private String actor_blog = "";
	private String actor_location = "";
	private String actor_email = "";
	private String actor_company = "";
	
	//*************************
	
	
	//******repo******
	private long repo_id;
	private String repo_name = "";
	
	private String repo_Url = "";
	
	
	//private String description ="";
	private int repo_watchers;
	private int repo_stargazers;
	private int repo_forks;
	
	private int repo_size;
	private String repo_owner = "";
	//@SerializedName("fork")
	private boolean repo_is_forked;
	//@SerializedName("private")
	private boolean repo_is_private;
	private int repo_open_issues;
	private boolean repo_has_issues;
	private boolean repo_has_downloads;
	private boolean repo_has_wiki;
	private String repo_language;
	private String repo_repoCreated_at;
	private String repo_pushed_at;
	private String repo_updated_at;
	private String repo_master_branch;
	

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("event");
		return sb.toString();
	}


	public long getRepo_id() {
		return repo_id;
	}


	public void setRepo_id(long repo_id) {
		this.repo_id = repo_id;
	}


	public Long getEvent_id() {
		return event_id;
	}


	public void setEvent_id(Long event_id) {
		this.event_id = event_id;
	}


	public String getEvent_url() {
		return event_url;
	}


	public void setEvent_url(String event_url) {
		this.event_url = event_url;
	}


	public String getEvent_type() {
		return event_type;
	}


	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}


	public String getEvent_created_at() {
		return event_created_at;
	}


	public void setEvent_created_at(String event_created_at) {
		this.event_created_at = event_created_at;
	}


	public boolean isEvent_is_public() {
		return event_is_public;
	}


	public void setEvent_is_public(boolean event_is_public) {
		this.event_is_public = event_is_public;
	}


	public String getGravatar_id() {
		return gravatar_id;
	}


	public void setGravatar_id(String gravatar_id) {
		this.gravatar_id = gravatar_id;
	}


	public String getActor_login() {
		return actor_login;
	}


	public void setActor_login(String actor_login) {
		this.actor_login = actor_login;
	}


	public String getActor_type() {
		return actor_type;
	}


	public void setActor_type(String actor_type) {
		this.actor_type = actor_type;
	}


	public String getActor_name() {
		return actor_name;
	}


	public void setActor_name(String actor_name) {
		this.actor_name = actor_name;
	}


	public String getActor_blog() {
		return actor_blog;
	}


	public void setActor_blog(String actor_blog) {
		this.actor_blog = actor_blog;
	}


	public String getActor_location() {
		return actor_location;
	}


	public void setActor_location(String actor_location) {
		this.actor_location = actor_location;
	}


	public String getActor_email() {
		return actor_email;
	}


	public void setActor_email(String actor_email) {
		this.actor_email = actor_email;
	}


	public String getActor_company() {
		return actor_company;
	}


	public void setActor_company(String actor_company) {
		this.actor_company = actor_company;
	}


	public String getRepo_name() {
		return repo_name;
	}


	public void setRepo_name(String repo_name) {
		this.repo_name = repo_name;
	}


	public String getRepo_Url() {
		return repo_Url;
	}


	public void setRepo_Url(String repo_Url) {
		this.repo_Url = repo_Url;
	}


	public int getRepo_watchers() {
		return repo_watchers;
	}


	public void setRepo_watchers(int repo_watchers) {
		this.repo_watchers = repo_watchers;
	}


	public int getRepo_stargazers() {
		return repo_stargazers;
	}


	public void setRepo_stargazers(int repo_stargazers) {
		this.repo_stargazers = repo_stargazers;
	}


	public int getRepo_forks() {
		return repo_forks;
	}


	public void setRepo_forks(int repo_forks) {
		this.repo_forks = repo_forks;
	}


	public int getRepo_size() {
		return repo_size;
	}


	public void setRepo_size(int repo_size) {
		this.repo_size = repo_size;
	}


	public String getRepo_owner() {
		return repo_owner;
	}


	public void setRepo_owner(String repo_owner) {
		this.repo_owner = repo_owner;
	}


	public boolean isRepo_is_forked() {
		return repo_is_forked;
	}


	public void setRepo_is_forked(boolean repo_is_forked) {
		this.repo_is_forked = repo_is_forked;
	}


	public boolean isRepo_is_private() {
		return repo_is_private;
	}


	public void setRepo_is_private(boolean repo_is_private) {
		this.repo_is_private = repo_is_private;
	}


	public int getRepo_open_issues() {
		return repo_open_issues;
	}


	public void setRepo_open_issues(int repo_open_issues) {
		this.repo_open_issues = repo_open_issues;
	}


	public boolean isRepo_has_issues() {
		return repo_has_issues;
	}


	public void setRepo_has_issues(boolean repo_has_issues) {
		this.repo_has_issues = repo_has_issues;
	}


	public boolean isRepo_has_downloads() {
		return repo_has_downloads;
	}


	public void setRepo_has_downloads(boolean repo_has_downloads) {
		this.repo_has_downloads = repo_has_downloads;
	}


	public boolean isRepo_has_wiki() {
		return repo_has_wiki;
	}


	public void setRepo_has_wiki(boolean repo_has_wiki) {
		this.repo_has_wiki = repo_has_wiki;
	}


	public String getRepo_language() {
		return repo_language;
	}


	public void setRepo_language(String repo_language) {
		this.repo_language = repo_language;
	}


	public String getRepo_repoCreated_at() {
		return repo_repoCreated_at;
	}


	public void setRepo_repoCreated_at(String repo_repoCreated_at) {
		this.repo_repoCreated_at = repo_repoCreated_at;
	}


	public String getRepo_pushed_at() {
		return repo_pushed_at;
	}


	public void setRepo_pushed_at(String repo_pushed_at) {
		this.repo_pushed_at = repo_pushed_at;
	}


	public String getRepo_updated_at() {
		return repo_updated_at;
	}


	public void setRepo_updated_at(String repo_updated_at) {
		this.repo_updated_at = repo_updated_at;
	}


	public String getRepo_master_branch() {
		return repo_master_branch;
	}


	public void setRepo_master_branch(String repo_master_branch) {
		this.repo_master_branch = repo_master_branch;
	}
	
	
}
