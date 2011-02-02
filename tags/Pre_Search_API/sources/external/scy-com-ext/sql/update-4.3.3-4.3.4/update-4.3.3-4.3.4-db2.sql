alter table BlogsEntry add urlTitle varchar(150);

create table BlogsStatsUser (
	statsUserId bigint not null primary key,
	groupId bigint,
	companyId bigint,
	userId bigint,
	entryCount integer,
	lastPostDate timestamp,
	ratingsTotalEntries integer,
	ratingsTotalScore double,
	ratingsAverageScore double
);

alter table BlogsStatsUser add ratingsTotalEntries integer;
alter table BlogsStatsUser add ratingsTotalScore double;
alter table BlogsStatsUser add ratingsAverageScore double;

delete from MBStatsUser where groupId = 0;
