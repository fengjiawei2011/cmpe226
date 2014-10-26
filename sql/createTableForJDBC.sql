--actor
create table actor (actor_id SERIAL PRIMARY KEY, blog varchar(255), company varchar(255), email varchar(255), gravatar_id varchar(255), location varchar(255), login varchar(255) not null, name varchar(255), type varchar(255));

--repository
create table repository (created_at varchar(255), forks int, has_downloads boolean, has_issues boolean, has_wiki boolean, is_forked boolean, is_private boolean, language varchar(255), master_branch varchar(255), name varchar(255), open_issues int, owner varchar(255), pushed_at varchar(255), size int, stargazers int, updated_at varchar(255), url varchar(255), watchers int, repo_id bigint PRIMARY KEY);

--event
create table event (actor_id bigint, created_at varchar(255), is_public boolean, repo_id bigint, type varchar(255), url varchar(1024), event_id SERIAL PRIMARY KEY, CONSTRAINT event_actor_id_fkey FOREIGN KEY(actor_id) REFERENCES actor (actor_id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION, CONSTRAINT event_repo_id_fkey FOREIGN KEY(repo_id) REFERENCES repository(repo_id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION);

--event_single_table
create table event_single_table (actor_blog text, actor_company varchar(255), actor_email varchar(255), actor_location varchar(255), actor_login varchar(255), actor_name varchar(255), actor_type varchar(255), event_created_at varchar(255), event_is_public boolean, event_type varchar(255), event_url varchar(1024), gravatar_id varchar(255), repo_Url varchar(255), repo_forks int, repo_has_downloads boolean, repo_has_issues boolean, repo_has_wiki boolean, repo_id bigint, repo_is_forked boolean, repo_is_private boolean, repo_language varchar(255), repo_master_branch varchar(255), repo_name varchar(255), repo_open_issues int, repo_owner varchar(255), repo_pushed_at varchar(255), repo_repoCreated_at varchar(255), repo_size int, repo_stargazers int, repo_updated_at varchar(255), repo_watchers int, event_id SERIAL PRIMARY KEY);
