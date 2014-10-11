
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