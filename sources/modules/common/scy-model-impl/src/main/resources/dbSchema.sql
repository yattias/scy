CREATE TABLE `scenario` (
	`primKey` varchar(55) NOT NULL default '',
	`name` varchar(250) default NULL,
	`description` text,
    `timeCreated` bigint(20) NOT NULL default '0',
	PRIMARY KEY  (`primKey`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
