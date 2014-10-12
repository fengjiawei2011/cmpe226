
-- 1.for the actor that works on largest number of repository
select actor_id, count(repo_id) as number_of_worked_repo 
from event
group by actor_id
order by number_of_worked_repo desc
limit 10;

-- 2. the most popular language:
-- the language that largest number of actor works on 

select r.language, count(distinct(actor_id)) as number_of_users
from repository r, event e
where r.repo_id=e.repo_id
group by r.language
order by number_of_users desc
limit 10;

select r.language, count(actor_id) as number_of_users
from repository r, event e
where r.repo_id=e.repo_id
group by r.language
order by number_of_users desc
limit 10;

-- 3. large result query:
-- find all the actors that have worked on JavaScript

select a.actor_id, a.login, a.email, count(e.event_id) as number_of_events
from repository r, event e, actor a
where r.repo_id=e.repo_id and e.actor_id=a.actor_id and r.language='JavaScript'
group by a.actor_id 
order by number_of_events desc
limit 10;

select a.actor_id, a.login, a.email, count(e.event_id) as number_of_events
from repository r, event e, actor a
where r.repo_id=e.repo_id and e.actor_id=a.actor_id and r.language='JavaScript'
group by a.actor_id 
order by number_of_events desc;

