alter table Group_ add typeSettings longvarchar null;

alter table SCProductEntry add tags varchar(300) null;
alter table SCProductEntry add author varchar(75) null;

create table SCProductScreenshot (
	productScreenshotId bigint not null primary key,
	companyId bigint,
	groupId bigint,
	productEntryId bigint,
	thumbnailId bigint,
	fullImageId bigint,
	priority int
);

alter table TagsAsset add description longvarchar null;
alter table TagsAsset add summary longvarchar null;
