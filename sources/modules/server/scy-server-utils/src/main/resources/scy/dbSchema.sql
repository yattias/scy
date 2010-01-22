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
	`participatesInScenario_primKey` varchar(55) NOT NULL default '',
	`name` varchar(250) default NULL,
	`description` text,
    `timeCreated` bigint(20) NOT NULL default '0',
    `xPos` bigint(20) NOT NULL default '0',
    `yPos` bigint(20) NOT NULL default '0',
    `inputAnchorELO_primKey` varchar(55) default NULL,
    `assessment_primKey` varchar(55) default NULL,
    PRIMARY KEY  (`primKey`),
    KEY `input_anchor_elo_key` (`inputAnchorELO_primKey`),
    KEY `participates_in_scenario_key` (`participatesInScenario_primKey`),
    KEY `las_assessment_key` (`assessment_primKey`),
    CONSTRAINT `las_input_anchor_elo` FOREIGN KEY (`inputAnchorELO_primKey`) REFERENCES `anchorelo` (`primKey`),
    CONSTRAINT `las_participates_in_scenario` FOREIGN KEY (`participatesInScenario_primKey`) REFERENCES `scenario` (`primKey`),
    CONSTRAINT `las_assessment_const` FOREIGN KEY (`assessment_primKey`) REFERENCES `assessment` (`primKey`)
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
    `las_primKey` varchar(55) default NULL,
    `tool_primKey` varchar(55) default NULL,
	PRIMARY KEY  (`primKey`),
    KEY `tooconfigactivity_key` (`activity_primKey`),
    KEY `tooconfigtool_key` (`tool_primKey`),
    KEY `tooconfiglas_key` (`las_primKey`),
    CONSTRAINT `toolconfiguration_activity` FOREIGN KEY (`activity_primKey`) REFERENCES `activity` (`primKey`),
    CONSTRAINT `toolconfiguration_tool` FOREIGN KEY (`tool_primKey`) REFERENCES `tool` (`primKey`),
    CONSTRAINT `toolconfiguration_las` FOREIGN KEY (`las_primKey`) REFERENCES `learningactivityspace` (`primKey`)
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

DROP TABLE IF EXISTS `learninggoal`;
CREATE TABLE `learninggoal` (
	`primKey` varchar(55) NOT NULL default '',
	`name` varchar(250) default NULL,
	`description` text,
    `timeCreated` bigint(20) NOT NULL default '0',
    `mission_primKey` varchar(55) default NULL ,
	PRIMARY KEY  (`primKey`),
	KEY `learninggoaltomission` (`mission_primKey`),
	CONSTRAINT `learninggoaltomissionconst` FOREIGN KEY (`mission_primKey`) REFERENCES `mission` (`primKey`)
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
    `user_primKey` bigint(20) NULL,
    `pedagogicalPlan_primKey` varchar(55) default NULL,
	PRIMARY KEY  (`primKey`),
	KEY `studentplaneloToUser` (`user_primKey`),
	KEY `studentplaneloToPedPlanr` (`pedagogicalPlan_primKey`),
	CONSTRAINT `studentplaneloToPedPlanConst` FOREIGN KEY (`pedagogicalPlan_primKey`) REFERENCES `pedagogicalplan` (`primKey`)

  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `studentplannedactivity`;
CREATE TABLE `studentplannedactivity` (
	`primKey` varchar(55) NOT NULL default '',
	`name` varchar(250) default NULL,
	`description` text,
	`note` text,
	`startDate` datetime default NULL,
	`endDate` datetime default NULL,
    `timeCreated` bigint(20) NOT NULL default '0',
    `associatedelo_primKey` varchar(55) default NULL,
    `studentplanelo_primKey` varchar(55) default NULL,
	PRIMARY KEY  (`primKey`),
	KEY `plannedactivitytoelo` (`associatedelo_primKey`),
	KEY `plannedactivitytoplan` (`studentplanelo_primKey`),
	CONSTRAINT `plannedactivitytoeloconst` FOREIGN KEY (`associatedelo_primKey`) REFERENCES `anchorelo` (`primKey`),
	CONSTRAINT `plannedactivitytoplanconst` FOREIGN KEY (`studentplanelo_primKey`) REFERENCES `studentplanelo` (`primKey`)

  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;


set FOREIGN_KEY_CHECKS=1;





