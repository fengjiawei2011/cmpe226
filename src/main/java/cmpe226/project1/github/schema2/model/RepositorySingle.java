package cmpe226.project1.github.schema2.model;

import com.google.gson.annotations.SerializedName;

public class RepositorySingle {

	private long id;
	
	private String name = "";
	private String url = "";
	
	
	//private String description ="";
	private int watchers;
	private int stargazers;
	private int forks;
	
	private int size;
	private String owner = "";
	@SerializedName("fork")
	private boolean is_forked;
	@SerializedName("private")
	private boolean is_private;
	private int open_issues;
	private boolean has_issues;
	private boolean has_downloads;
	private boolean has_wiki;
	private String language;
	private String created_at;
	private String pushed_at;
	private String updated_at;
	private String master_branch;
	
	
	@Override
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("id:"+ id );
		sb.append(",name:'"+name+"'");
		sb.append(",url:"+url);
		//sb.append(",description:'"+description+"'");
		sb.append(",watchers:"+watchers);
		sb.append(",stargazers:"+stargazers);
		sb.append(",forks:"+forks);
		sb.append(",size:"+size);
		sb.append(",owner:'"+owner+"'");
		sb.append(",fork:"+is_forked);
		sb.append(",private:"+is_private);
		sb.append(",open_issues:"+open_issues);
		sb.append(",has_issues:"+has_issues);
		sb.append(",has_downloads:"+has_downloads);
		sb.append(",has_wiki:"+has_wiki);
		sb.append(",language:'"+language+"'");
		sb.append(",created_at:'"+created_at+"'");
		sb.append(",pushed_at:'"+pushed_at+"'");
		sb.append(",master_branch:'"+master_branch+"'");
		sb.append("}");
		return sb.toString();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getUrl() {
		return url;
	}



	public void setUrl(String url) {
		this.url = url;
	}



//	public String getDescription() {
//		return description;
//	}
//
//
//
//	public void setDescription(String description) {
//		this.description = description;
//	}



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



	public int getSize() {
		return size;
	}



	public void setSize(int size) {
		this.size = size;
	}



	public String getOwner() {
		return owner;
	}



	public void setOwner(String owner) {
		this.owner = owner;
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



	public String getLanguage() {
		return language;
	}



	public void setLanguage(String language) {
		this.language = language;
	}



	public String getCreated_at() {
		return created_at;
	}



	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}



	public String getPushed_at() {
		return pushed_at;
	}



	public void setPushed_at(String pushed_at) {
		this.pushed_at = pushed_at;
	}



	public String getUpdated_at() {
		return updated_at;
	}



	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}



	public String getMaster_branch() {
		return master_branch;
	}



	public void setMaster_branch(String master_branch) {
		this.master_branch = master_branch;
	}
	
	
}
