#sql("paginate")
	select f.id,
		substring(f.title, 1, 100) as title,
		substring(f.content, 1, 180) as content,
		a.avatar,
		a.id as accountId
	from feedback f inner join account a on f.accountId = a.id
	where report < #para(0)
	order by f.createAt desc
#end

#sql("findById")
	select f.* , a.avatar, a.nickName
	from feedback f inner join account a on f.accountId = a.id
	where f.id = #para(0) and f.report < #para(1) limit 1
#end

#sql("getHotFeedback")
	select distinct f.id, f.title
	from feedback_page_view fpv inner join feedback f on fpv.feedbackId = f.id
	where visitDate > #para(0) and f.report < #para(1)
	order by visitCount desc limit 10
#end

#sql("getReplyPage")
	select fr.*, a.nickName, a.avatar
	from feedback_reply fr inner join account a on fr.accountId = a.id
	where feedbackId = #para(0)
#end




