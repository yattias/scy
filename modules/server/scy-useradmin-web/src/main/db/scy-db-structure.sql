-- MySQL dump 10.11
--
-- Host: localhost    Database: scyuseradmin
-- ------------------------------------------------------
-- Server version	5.0.51a-community-nt

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `buddyconnection`
--

DROP TABLE IF EXISTS `buddyconnection`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `buddyconnection` (
  `primKey` varchar(55) NOT NULL,
  `name` varchar(255) default NULL,
  `myself_primKey` varchar(55) default NULL,
  `buddy_primKey` varchar(55) default NULL,
  `description` varchar(255) default NULL,
  PRIMARY KEY  (`primKey`),
  KEY `FK759DCE44A4FBDCBC` (`buddy_primKey`),
  KEY `FK759DCE4464E8FEEE` (`myself_primKey`),
  KEY `FKFECBF224BB263608` (`buddy_primKey`),
  KEY `FKFECBF2247B13583A` (`myself_primKey`),
  CONSTRAINT `FK759DCE4464E8FEEE` FOREIGN KEY (`myself_primKey`) REFERENCES `user` (`primKey`),
  CONSTRAINT `FK759DCE44A4FBDCBC` FOREIGN KEY (`buddy_primKey`) REFERENCES `user` (`primKey`),
  CONSTRAINT `FKFECBF2247B13583A` FOREIGN KEY (`myself_primKey`) REFERENCES `user` (`primKey`),
  CONSTRAINT `FKFECBF224BB263608` FOREIGN KEY (`buddy_primKey`) REFERENCES `user` (`primKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `project` (
  `primKey` varchar(55) NOT NULL,
  `name` varchar(255) default NULL,
  `description` varchar(255) default NULL,
  PRIMARY KEY  (`primKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `role` (
  `primKey` varchar(55) NOT NULL,
  `name` varchar(255) default NULL,
  `description` varchar(255) default NULL,
  PRIMARY KEY  (`primKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `scygroup`
--

DROP TABLE IF EXISTS `scygroup`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `scygroup` (
  `primKey` varchar(55) NOT NULL,
  `name` varchar(255) default NULL,
  `parentGroup_primKey` varchar(55) default NULL,
  `project_primKey` varchar(55) default NULL,
  `description` varchar(255) default NULL,
  PRIMARY KEY  (`primKey`),
  KEY `FKF38B6BF6354438DB` (`project_primKey`),
  KEY `FKF38B6BF664C694FD` (`parentGroup_primKey`),
  KEY `FKF38B6BF610451E8F` (`project_primKey`),
  KEY `FKF38B6BF6A93FC4B1` (`parentGroup_primKey`),
  CONSTRAINT `FKF38B6BF610451E8F` FOREIGN KEY (`project_primKey`) REFERENCES `project` (`primKey`),
  CONSTRAINT `FKF38B6BF6354438DB` FOREIGN KEY (`project_primKey`) REFERENCES `project` (`primKey`),
  CONSTRAINT `FKF38B6BF664C694FD` FOREIGN KEY (`parentGroup_primKey`) REFERENCES `scygroup` (`primKey`),
  CONSTRAINT `FKF38B6BF6A93FC4B1` FOREIGN KEY (`parentGroup_primKey`) REFERENCES `scygroup` (`primKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `user` (
  `primKey` varchar(55) NOT NULL,
  `name` varchar(255) default NULL,
  `enabled` varchar(255) default NULL,
  `firstName` varchar(255) default NULL,
  `lastName` varchar(255) default NULL,
  `password` varchar(255) default NULL,
  `userName` varchar(255) NOT NULL,
  `group_primKey` varchar(55) default NULL,
  `project_primKey` varchar(55) default NULL,
  `description` varchar(255) default NULL,
  PRIMARY KEY  (`primKey`),
  UNIQUE KEY `userName` (`userName`),
  KEY `FK36EBCB354438DB` (`project_primKey`),
  KEY `FK36EBCBFFCB967` (`group_primKey`),
  KEY `FK36EBCB10451E8F` (`project_primKey`),
  KEY `FK36EBCB5475E91B` (`group_primKey`),
  CONSTRAINT `FK36EBCB10451E8F` FOREIGN KEY (`project_primKey`) REFERENCES `project` (`primKey`),
  CONSTRAINT `FK36EBCB354438DB` FOREIGN KEY (`project_primKey`) REFERENCES `project` (`primKey`),
  CONSTRAINT `FK36EBCB5475E91B` FOREIGN KEY (`group_primKey`) REFERENCES `scygroup` (`primKey`),
  CONSTRAINT `FK36EBCBFFCB967` FOREIGN KEY (`group_primKey`) REFERENCES `scygroup` (`primKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `userrole`
--

DROP TABLE IF EXISTS `userrole`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `userrole` (
  `primKey` varchar(55) NOT NULL,
  `name` varchar(255) default NULL,
  `role_primKey` varchar(55) default NULL,
  `user_primKey` bigint default NULL,
  `description` varchar(255) default NULL,
  PRIMARY KEY  (`primKey`),
  KEY `FKF02B8EC1B2838F17` (`role_primKey`),
  KEY `FKF02B8EC1F6A440C1` (`user_primKey`),
  CONSTRAINT `FKF02B8EC1B2838F17` FOREIGN KEY (`role_primKey`) REFERENCES `role` (`primKey`),
  CONSTRAINT `FKF02B8EC1F6A440C1` FOREIGN KEY (`user_primKey`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `usersession`
--

DROP TABLE IF EXISTS `usersession`;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
CREATE TABLE `usersession` (
  `primKey` varchar(55) NOT NULL,
  `name` varchar(255) default NULL,
  `sessionStarted` bigint(20) NOT NULL,
  `user_primKey` varchar(55) default NULL,
  `sessionId` varchar(255) default NULL,
  `sessionEnded` bigint(20) default NULL,
  `sessionActive` bit(1) default NULL,
  `description` varchar(255) default NULL,
  PRIMARY KEY  (`primKey`),
  KEY `FK6452644BF6A440C1` (`user_primKey`),
  KEY `FK119CC6BCCE9A0D` (`user_primKey`),
  CONSTRAINT `FK119CC6BCCE9A0D` FOREIGN KEY (`user_primKey`) REFERENCES `user` (`primKey`),
  CONSTRAINT `FK6452644BF6A440C1` FOREIGN KEY (`user_primKey`) REFERENCES `user` (`primKey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET character_set_client = @saved_cs_client;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2009-03-03  8:34:59
