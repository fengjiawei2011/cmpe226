
-- Queries for 3rd Normal Form
-- There are 3 tables: event, actor, repository
-- Queries for 0 Normal Form
-- There are only one tables: event_single_table

--
-- 1.for the actor that works on largest number of repository
-- 

	-- 3NF
	select a.login, count(distinct(e.repo_id)) as number_of_worked_repo 
	from event e, actor a
	where e.actor_id=a.actor_id
	group by a.login
	order by number_of_worked_repo desc
	limit 10;

	-- 0NF
	select actor_login, count(distinct(repo_id)) as number_of_worked_repo 
	from event_single_table
	group by actor_login
	order by number_of_worked_repo desc
	limit 10;

--
-- 2. the most popular language: the language that largest number of actor works on
--

	-- 3NF
	select r.language, count(distinct(a.login)) as number_of_users
	from repository r, event e, actor a
	where r.repo_id=e.repo_id and e.actor_id=a.actor_id
	group by r.language
	order by number_of_users desc
	limit 10;

	-- 0NF
	select repo_language, count(distinct(actor_login)) as number_of_users
	from event_single_table
	group by repo_language
	order by number_of_users desc
	limit 10;

--
-- 3.how many unique actors 
--
	-- 3NF  --faster than 0NF
	select count(distinct(login))
	from actor;
	-- or  -- very fast
	select count(actor_id)
	from actor;

	-- 0NF  --slower than 3NF
	select count(distinct(actor_login))
	from event_single_table;

--
-- 4. the actor work hardest: find actors that have largest number of events
-- 

	-- 3NF
	select a.login, count(e.event_id) as number_of_events
	from repository r, event e, actor a
	where r.repo_id=e.repo_id and e.actor_id=a.actor_id 
	group by a.login 
	order by number_of_events desc
	limit 10;

	-- 0NF
	select actor_login, count(event_id) as number_of_events
	from event_single_table
	group by actor_login
	order by number_of_events desc
	limit 10;

-- 4.how many unique actors 

	-- 3NF  --faster than 0NF
	select count(distinct(login))
	from actor;
	-- or  -- very fast
	select count(actor_id)
	from actor;

	-- 0NF  --slower than 3NF
	select count(distinct(actor_login))
	from event_single_table;

