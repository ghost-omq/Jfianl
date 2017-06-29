#sql("paginate")
	select p.id,
		substring(p.title, 1, 100) as title,
		substring(p.content, 1, 180) as content,
		a.avatar,
		a.id as accountId
	from project p inner join account a on p.accountId = a.id
	where report < #para(0)
#end

#sql("findById")
	select p.* , a.avatar, a.nickName
	from project p inner join account a on p.accountId = a.id
	where p.id = #para(0) and p.report < #para(1)  limit 1
#end

#sql("findByIdWithColumns")
	select #(columns) from project where id = #para(id) and report < #para(report) limit 1
#end

#sql("getHotProject")
	select id, title from project  where report < #para(0) order by createAt asc limit 10
#end

#sql("getAllProject")
	select #(columns) from project where report < #para(report) order by createAt asc
#end





