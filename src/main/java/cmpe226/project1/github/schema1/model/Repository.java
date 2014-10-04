package cmpe226.project1.github.schema1.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="repository")
public class Repository {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "repo_id", updatable = false, nullable = false)
	private long id;
	
	@ManyToOne
	@JoinColumn(name="actor_id")
	private Actor owner;
	
	private String name;
	private String html_url;
	private Date created_at;
	private Date updated_at;
	private Date pushed_at;
	private int watchers;
	private int stargazers;
	private int forks;
	private boolean is_forked;
	private boolean is_private;
	private int open_issues;
	private boolean has_issues;
	private boolean has_downloads;
	private boolean has_wiki;
	private String master_branch;
	private String language;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Actor getOwner() {
		return owner;
	}
	public void setOwner(Actor owner) {
		this.owner = owner;
	}
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHtml_url() {
		return html_url;
	}
	public void setHtml_url(String html_url) {
		this.html_url = html_url;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public Date getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	public Date getPushed_at() {
		return pushed_at;
	}
	public void setPushed_at(Date pushed_at) {
		this.pushed_at = pushed_at;
	}
	public int getWatchers() {
		return watchers;
	}
	public void setWatchers(int watchers) {
		this.watchers = watchers;
	}
	public int getStargazers() {
		return stargazers;
	}
	public void setStargazers(int stargazers) {
		this.stargazers = stargazers;
	}
	public int getForks() {
		return forks;
	}
	public void setForks(int forks) {
		this.forks = forks;
	}
	public boolean isIs_forked() {
		return is_forked;
	}
	public void setIs_forked(boolean is_forked) {
		this.is_forked = is_forked;
	}
	public boolean isIs_private() {
		return is_private;
	}
	public void setIs_private(boolean is_private) {
		this.is_private = is_private;
	}
	public int getOpen_issues() {
		return open_issues;
	}
	public void setOpen_issues(int open_issues) {
		this.open_issues = open_issues;
	}
	public boolean isHas_issues() {
		return has_issues;
	}
	public void setHas_issues(boolean has_issues) {
		this.has_issues = has_issues;
	}
	public boolean isHas_downloads() {
		return has_downloads;
	}
	public void setHas_downloads(boolean has_downloads) {
		this.has_downloads = has_downloads;
	}
	public boolean isHas_wiki() {
		return has_wiki;
	}
	public void setHas_wiki(boolean has_wiki) {
		this.has_wiki = has_wiki;
	}
	public String getMaster_branch() {
		return master_branch;
	}
	public void setMaster_branch(String master_branch) {
		this.master_branch = master_branch;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	
	
}
