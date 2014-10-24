--actor
create table actor (actor_id SERIAL PRIMARY KEY, blog varchar(255), company varchar(255), email varchar(255), gravatar_id varchar(255), location varchar(255), login varchar(255) not null, name varchar(255), type varchar(255));

--repository
create table event (actor_id bigint, created_at varchar(255), is_public boolean, repo_id bigint, type varchar(255), url varchar(255), event_id SERIAL PRIMARY KEY);

--event
create table event (actor_id bigint, created_at varchar(255), is_public boolean, repo_id bigint, type varchar(255), url varchar(255), event_id SERIAL PRIMARY KEY, CONSTRAINT event_actor_id_fkey FOREIGN KEY(actor_id) REFERENCES actor (actor_id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION, CONSTRAINT event_repo_id_fkey FOREIGN KEY(repo_id) REFERENCES repository(repo_id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION);
