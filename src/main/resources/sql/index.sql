#sql("getProjectList")
	select p.id,
		substring(p.title, 1, 100) as title,
		substring(p.content, 1, 180) as content,
		a.avatar,
		a.id as accountId
	from project p inner join account a on p.accountId = a.id
	where report < #para(0)
	order by p.id asc limit 10
#end

#sql("getShareList")
	select s.id,
		substring(s.title, 1, 100) as title,
		substring(s.content, 1, 180) as content,
		a.avatar,
		a.id as accountId
	from share s inner join account a on s.accountId = a.id
	where report < #para(0)
	order by s.createAt desc limit 10
#end

#sql("getFeedbackList")
	select f.id,
		substring(f.title, 1, 100) as title,
		substring(f.content, 1, 180) as content,
		a.avatar,
		a.id as accountId
	from feedback f inner join account a on f.accountId = a.id
	where report < #para(0)
	order by f.createAt desc limit 10
#end






