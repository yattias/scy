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
	`missionMapId` varchar(250) default NULL,
	`description` text,
    `timeCreated` bigint(20) NOT NULL default '0',
    `xPos` bigint(20) NOT NULL default '0',
    `yPos` bigint(20) NOT NULL default '0',
    `inputTo_primKey` varchar(55) default NULL,
    `outputFrom_primKey` varchar(55) default NULL,
    `producedBy_primKey` varchar(55) default NULL,
    `includedInPortfolio` tinyint(1) default 0,
    `obligatoryInPortfolio` tinyint(1) default 0,
    PRIMARY KEY  (`primKey`),
    KEY `inputTo_primKey_key` (`inputTo_primKey`),
    KEY `outputFrom_primKey_key` (`outputFrom_primKey`),
    KEY `producedBy_primKey_key` (`producedBy_primKey`),
    CONSTRAINT `anchorelo_las` FOREIGN KEY (`inputTo_primKey`) REFERENCES `learningactivityspace` (`primKey`),
    CONSTRAINT `outputFrom_const_las` FOREIGN KEY (`outputFrom_primKey`) REFERENCES `learningactivityspace` (`primKey`),
    CONSTRAINT `producedBy_primKey_const_las` FOREIGN KEY (`producedBy_primKey`) REFERENCES `activity` (`primKey`)
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
    `autoaddToStudentPlan` tinyint(1) default 0,
    `expectedDurationInMinutes` int(8) default 0,
    `startDate` date default NULL,
    `endDate` date default NULL,
    `startTime` time default NULL,
    `endTime` time default NULL,
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

DROP TABLE IF EXISTS `lasconfiguration`;
CREATE TABLE `lasconfiguration` (
	`primKey` varchar(55) NOT NULL default '',
	`name` varchar(250) default NULL,
	`description` text,
    `timeCreated` bigint(20) NOT NULL default '0',
    `configurationtype` varchar(250) default NULL,
    `las_primKey` varchar(55) default NULL,
    `agent_primKey` varchar(55) default NULL,
	PRIMARY KEY  (`primKey`),
    KEY `lasconfiguration_key` (`las_primKey`),
    KEY `lasconfiguration_agent_key` (`agent_primKey`),
    CONSTRAINT `lasconfiguration_cons` FOREIGN KEY (`las_primKey`) REFERENCES `learningactivityspace` (`primKey`),
    CONSTRAINT `lasconfiguration_agent_cons` FOREIGN KEY (`agent_primKey`) REFERENCES `agent` (`primKey`)
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
	`overallSCYLabScaffoldingLevel` varchar(250) default '1',
	`overallMissionContentScaffoldingLevel` varchar(250) default '1',	
	`description` text,
	`published` tinyint(1) default 0,
	`maximumNumberOfAnchorELOsInPortfolio` tinyint(2) default 0,
	`minimumNumberOfAnchorELOsInPortfolio` tinyint(2) default 0,
	`makeAllAssignedStudentsBuddies` tinyint(1) default 0,
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

DROP TABLE IF EXISTS `assignedpedagogicalplan`;
CREATE TABLE `assignedpedagogicalplan` (
	`primKey` varchar(55) NOT NULL default '',
	`name` varchar(250) default NULL,
	`description` text,
    `timeCreated` bigint(20) NOT NULL default '0',
    `pedagogicalPlan_primKey` varchar(55) default NULL,
    `user_primKey` bigint(20) NULL,
	PRIMARY KEY  (`primKey`),
    KEY `assPedPlanToPedPlan` (`pedagogicalPlan_primKey`),
    KEY `assignedPedagogicalPlanUser` (`user_primKey`),
    CONSTRAINT `constAssPedPlanToTemplate` FOREIGN KEY (`pedagogicalPlan_primKey`) REFERENCES `pedagogicalplan` (`primKey`),
    CONSTRAINT `constAssignedPedagogicalPlanUser` FOREIGN KEY (`user_primKey`) REFERENCES `users` (`id`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `mission`;
CREATE TABLE `mission` (
	`primKey` varchar(55) NOT NULL default '',
	`name` varchar(250) default NULL,
	`description` text,
	`missionOutline` text,
	`targetGroup` text,	
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
	UNIQUE KEY `uniqueKey` (`studentplanelo_primKey`,`associatedelo_primKey`),
	KEY `plannedactivitytoelo` (`associatedelo_primKey`),
	KEY `plannedactivitytoplan` (`studentplanelo_primKey`),
	CONSTRAINT `plannedactivitytoeloconst` FOREIGN KEY (`associatedelo_primKey`) REFERENCES `anchorelo` (`primKey`),
	CONSTRAINT `plannedactivitytoplanconst` FOREIGN KEY (`studentplanelo_primKey`) REFERENCES `studentplanelo` (`primKey`)

  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `agent`;
CREATE TABLE `agent` (
	`primKey` varchar(55) NOT NULL default '',
	`name` varchar(250) default NULL,
	`description` text,
    `timeCreated` bigint(20) NOT NULL default '0',
	PRIMARY KEY  (`primKey`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `server`;
CREATE TABLE `server` (
	`primKey` varchar(55) NOT NULL default '',
	`name` varchar(250) default NULL,
	`description` text,
    `timeCreated` bigint(20) NOT NULL default '0',
    `serverCSS` varchar(250) default NULL,
	PRIMARY KEY  (`primKey`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `studentplannedactivities_related_to_users`;
CREATE TABLE `studentplannedactivities_related_to_users` (
	`user_fk` bigint(20) NULL,
	`studentplannedactivity_fk` varchar(55) NOT NULL default '',
	PRIMARY KEY (`user_fk`,`studentplannedactivity_fk`),
	KEY  `user_fk_key` (`user_fk`),
	KEY  `studentplannedactivity_fk_key` (`studentplannedactivity_fk`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `pedagogicalplan_related_to_users`;
CREATE TABLE `pedagogicalplan_related_to_users` (
	`user_fk` bigint(20) NULL,
	`pedagogicalplan_fk` varchar(55) NOT NULL default '',
	PRIMARY KEY (`user_fk`,`pedagogicalplan_fk`),
	KEY  `user_fk_key` (`user_fk`),
	KEY  `pedagogicalplan_fk_key` (`pedagogicalplan_fk`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS `eloref`;
CREATE TABLE `eloref` (
	`primKey` varchar(55) NOT NULL default '',
	`name` varchar(250) default NULL,
	`description` text,
    `timeCreated` bigint(20) NOT NULL default '0',

	`elo_uri` varchar(200) DEFAULT NULL,
	`title` varchar(128) DEFAULT NULL,
	`image` varchar(100) DEFAULT NULL,
	`tool` varchar(128) DEFAULT NULL,
	`type` varchar(128) DEFAULT NULL,
	`topic` varchar(128) DEFAULT NULL,
	`user_primKey` bigint(20) DEFAULT NULL,
	`date` datetime DEFAULT NULL,
	`version` int(11) DEFAULT NULL,
	`mission_primKey` varchar(55) default NULL,

  	KEY `eloRefUser` (`user_primKey`),
  	KEY `eloRefMission` (`mission_primKey`),
    CONSTRAINT `eloRefMission` FOREIGN KEY (`mission_primKey`) REFERENCES `mission` (`primKey`),
  	CONSTRAINT `eloRefUser` FOREIGN KEY (`user_primKey`) REFERENCES `users` (`id`),
	PRIMARY KEY  (`primKey`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `playful_assessment`;
CREATE TABLE `playful_assessment` (
	`primKey` varchar(55) NOT NULL default '',
	`name` varchar(250) default NULL,
	`description` text,
    `timeCreated` bigint(20) NOT NULL default '0',

	`eloref_primKey` varchar(55) DEFAULT NULL,
	`comment` text DEFAULT NULL,
	`date` datetime DEFAULT NULL,
	`reviewer_primKey` bigint(20) DEFAULT NULL,
	`score` int(11) DEFAULT NULL,

	PRIMARY KEY  (`primKey`),
	KEY `assessmentRefEloRef` (`eloref_primKey`),
	KEY `assessmentRefReviewer` (`reviewer_primKey`),
	CONSTRAINT `assessmentRefEloRef` FOREIGN KEY (`eloref_primKey`) REFERENCES `eloref` (`primKey`),
  	CONSTRAINT `assessmentRefReviewer` FOREIGN KEY (`reviewer_primKey`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `filedata`;
CREATE TABLE `filedata` (
	`primKey` varchar(55) NOT NULL default '',
	`name` varchar(250) default NULL,
	`originalName` varchar(250) default NULL,
	`contentType` varchar(250) default NULL,
	`description` text,
    `timeCreated` bigint(20) NOT NULL default '0',
    `file` longblob,
    `size` bigint(20) NOT NULL default '0',
	PRIMARY KEY  (`primKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `fileref`;
CREATE TABLE `fileref` (
	`primKey` varchar(55) NOT NULL default '',
	`name` varchar(250) default NULL,
	`fileType` varchar(250) default NULL,
	`description` text,
    `timeCreated` bigint(20) NOT NULL default '0',
    `fileData_primKey` varchar(55) default NULL,
    `iconFileData_primKey` varchar(55) default NULL,
    PRIMARY KEY  (`primKey`),
    KEY `fileRefRef` (`fileData_primKey`),
    KEY `iconFileDataRef` (`iconFileData_primKey`),
    CONSTRAINT `fileRefRefRef` FOREIGN KEY (`fileData_primKey`) REFERENCES `filedata` (`primKey`),
    CONSTRAINT `iconFileDataRef_const` FOREIGN KEY (`iconFileData_primKey`) REFERENCES `filedata` (`primKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `setup`;
CREATE TABLE `setup` (
	`primKey` varchar(55) NOT NULL default '',
	`name` varchar(250) default NULL,
	`description` text,
    `timeCreated` bigint(20) NOT NULL default '0',
    PRIMARY KEY  (`primKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `runtimeaction`;
CREATE TABLE `runtimeaction` (
	`primKey` varchar(55) NOT NULL default '',
	`name` varchar(250) default NULL,
	`description` text,
    `timeCreated` bigint(20) NOT NULL default '0',
	`actionType` varchar(250) default NULL,
	`tool` varchar(250) default NULL,
	`session` varchar(250) default NULL,
	`mission` varchar(250) default NULL,
	`runtimeactiontype` varchar(55) default NULL,
	`actionId` varchar(250) default NULL,
    `timeInMillis` bigint(20) NOT NULL default '0',
    `user_primKey` bigint(20) DEFAULT NULL,
    KEY `runtimeactionrefuser` (`user_primKey`),
    CONSTRAINT `runtimeactionrefuserconst` FOREIGN KEY (`user_primKey`) REFERENCES `users` (`id`),
    PRIMARY KEY  (`primKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




set FOREIGN_KEY_CHECKS=1;





