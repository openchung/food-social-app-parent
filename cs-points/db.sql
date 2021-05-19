SELECT
t1.fk_diner_id,
sum(t1.points) total,
rank() over (order by sum(t1.points) DESC) ranks,
t2.nickname,
t2.avatar_url
FROM dinerdb.t_diner_points t1
LEFT JOIN t_diners t2 ON t1.fk_diner_id = t2.id
WHERE t1.is_valid = 1 AND t2.is_valid = 1
GROUP BY t1.fk_diner_id
order by total DESC LIMIT 20
;


select id, total, ranks, nickname, avatar_url FROM (
SELECT
t1.fk_diner_id id,
sum(t1.points) total,
rank() over (order by sum(t1.points) DESC) ranks,
t2.nickname,
t2.avatar_url
FROM dinerdb.t_diner_points t1
LEFT JOIN t_diners t2 ON t1.fk_diner_id = t2.id
WHERE t1.is_valid = 1 AND t2.is_valid = 1
GROUP BY t1.fk_diner_id
order by total DESC) r where id = 5;
