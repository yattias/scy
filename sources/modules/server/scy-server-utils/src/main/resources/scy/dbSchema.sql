set FOREIGN_KEY_CHECKS=0;

set character_set_connection=utf8;
set character_set_results=utf8;
set character_set_client=utf8;


DROP TABLE IF EXISTS `scenario`;
CREATE TABLE `scenario` (
	`primKey` varchar(55) NOT NULL default '',
	`name` varchar(250) default NULL,
	`description` text,
    `timeCreated` bigint(20) NOT NULL default '0',
    `learningActivitySpace_primKey` varchar(55) default NULL,
	PRIMARY KEY  (`primKey`),
    KEY `las_key` (`learningActivitySpace_primKey`),
    CONSTRAINT `scenario_las` FOREIGN KEY (`learningActivitySpace_primKey`) REFERENCES `learningactivityspace` (`primKey`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `anchorelo`;
CREATE TABLE `anchorelo` (
	`primKey` varchar(55) NOT NULL default '',
	`name` varchar(250) default NULL,
	`description` text,
    `timeCreated` bigint(20) NOT NULL default '0',
    `xPos` bigint(20) NOT NULL default '0',
    `yPos` bigint(20) NOT NULL default '0',
    `inputTo_primKey` varchar(55) default NULL,
	PRIMARY KEY  (`primKey`),
    KEY `inputTo_primKey_key` (`inputTo_primKey`),
    CONSTRAINT `anchorelo_las` FOREIGN KEY (`inputTo_primKey`) REFERENCES `learningactivityspace` (`primKey`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `learningactivityspace`;
CREATE TABLE `learningactivityspace` (
	`primKey` varchar(55) NOT NULL default '',
	`name` varchar(250) default NULL,
	`description` text,
    `timeCreated` bigint(20) NOT NULL default '0',
    `xPos` bigint(20) NOT NULL default '0',
    `yPos` bigint(20) NOT NULL default '0',
    `inputAnchorELO_primKey` varchar(55) default NULL,
	PRIMARY KEY  (`primKey`),
    KEY `input_anchor_elo_key` (`inputAnchorELO_primKey`),
    CONSTRAINT `las_input_anchor_elo` FOREIGN KEY (`inputAnchorELO_primKey`) REFERENCES `anchorelo` (`primKey`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity` (
	`primKey` varchar(55) NOT NULL default '',
	`name` varchar(250) default NULL,
	`description` text,
    `timeCreated` bigint(20) NOT NULL default '0',
    `learningActivitySpace_primKey` varchar(55) default NULL,
    `anchorElo_primKey` varchar(55) default NULL,
	PRIMARY KEY  (`primKey`),
    KEY `activitylas_key` (`learningActivitySpace_primKey`),
    KEY `anchorElo_primKey_key` (`anchorElo_primKey`),
    CONSTRAINT `activity_las` FOREIGN KEY (`learningActivitySpace_primKey`) REFERENCES `learningactivityspace` (`primKey`),
    CONSTRAINT `anchorElo_primKey_const` FOREIGN KEY (`anchorElo_primKey`) REFERENCES `anchorelo` (`primKey`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `toolconfiguration`;
CREATE TABLE `toolconfiguration` (
	`primKey` varchar(55) NOT NULL default '',
	`name` varchar(250) default NULL,
	`description` text,
    `timeCreated` bigint(20) NOT NULL default '0',
    `toolid` varchar(250) default NULL,
    `activity_primKey` varchar(55) default NULL,
    `tool_primKey` varchar(55) default NULL,
	PRIMARY KEY  (`primKey`),
    KEY `tooconfigactivity_key` (`activity_primKey`),
    KEY `tooconfigtool_key` (`tool_primKey`),
    CONSTRAINT `toolconfiguration_activity` FOREIGN KEY (`activity_primKey`) REFERENCES `activity` (`primKey`),
    CONSTRAINT `toolconfiguration_tool` FOREIGN KEY (`tool_primKey`) REFERENCES `tool` (`primKey`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `tool`;
CREATE TABLE `tool` (
	`primKey` varchar(55) NOT NULL default '',
	`name` varchar(250) default NULL,
	`description` text,
    `timeCreated` bigint(20) NOT NULL default '0',
    `learningActivitySpace_primKey` varchar(55) default NULL,
	PRIMARY KEY  (`primKey`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `pedagogicalplan`;
CREATE TABLE `pedagogicalplan` (
	`primKey` varchar(55) NOT NULL default '',
	`name` varchar(250) default NULL,
	`pedtype` varchar(250) default NULL,
	`description` text,
    `timeCreated` bigint(20) NOT NULL default '0',
    `pedagogicalPlanTemplate_primKey` varchar(55) default NULL,
    `scenario_primKey` varchar(55) default NULL,
    `mission_primKey` varchar(55) default NULL,
	PRIMARY KEY  (`primKey`),
    KEY `pedPlanToTemplate` (`pedagogicalPlanTemplate_primKey`),
    KEY `pedPlanToScenario` (`scenario_primKey`),
    KEY `pedPlanToMission` (`mission_primKey`),
    CONSTRAINT `pedagogicalPlanRefTemplate` FOREIGN KEY (`pedagogicalPlanTemplate_primKey`) REFERENCES `pedagogicalplan` (`primKey`),
    CONSTRAINT `pedagogicalPlanRefScenario` FOREIGN KEY (`scenario_primKey`) REFERENCES `scenario` (`primKey`),
    CONSTRAINT `pedagogicalPlanRefMission` FOREIGN KEY (`mission_primKey`) REFERENCES `mission` (`primKey`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `mission`;
CREATE TABLE `mission` (
	`primKey` varchar(55) NOT NULL default '',
	`name` varchar(250) default NULL,
	`description` text,
    `timeCreated` bigint(20) NOT NULL default '0',
	PRIMARY KEY  (`primKey`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `assessment`;
CREATE TABLE `assessment` (
	`primKey` varchar(55) NOT NULL default '',
	`name` varchar(250) default NULL,
	`description` text,
    `timeCreated` bigint(20) NOT NULL default '0',
    `assessmentStrategy_primKey` varchar(55) default NULL,
	PRIMARY KEY  (`primKey`),
    KEY `assessmentToAStrategy` (`assessmentStrategy_primKey`),
    CONSTRAINT `assessmentToAStrategyConst` FOREIGN KEY (`assessmentStrategy_primKey`) REFERENCES `assessmentstrategy` (`primKey`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `assessmentstrategy`;
CREATE TABLE `assessmentstrategy` (
	`primKey` varchar(55) NOT NULL default '',
	`name` varchar(250) default NULL,
	`strategytype` varchar(250) default NULL,
	`description` text,
    `timeCreated` bigint(20) NOT NULL default '0',
	PRIMARY KEY  (`primKey`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `studentplanelo`;
CREATE TABLE `studentplanelo` (
	`primKey` varchar(55) NOT NULL default '',
	`name` varchar(250) default NULL,
	`description` text,
    `timeCreated` bigint(20) NOT NULL default '0',
    `user_primKey` varchar(55) NOT NULL default '',
	PRIMARY KEY  (`primKey`),
	KEY `studentplaneloToUser` (`user_primKey`),
	CONSTRAINT `studentplaneloToUserConst` FOREIGN KEY (`user_primKey`) REFERENCES `user` (`primKey`)

  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;


set FOREIGN_KEY_CHECKS=1;





