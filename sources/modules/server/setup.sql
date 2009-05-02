CREATE DATABASE IF NOT EXISTS `scy` ;
CREATE USER 'scyuseradmin'@'localhost' IDENTIFIED BY 'scyuseradmin';
GRANT ALL PRIVILEGES ON `scy`. * TO 'scyuseradmin'@'localhost';

CREATE DATABASE IF NOT EXISTS `scysqlspaces` ;
CREATE USER 'scysqlspaces'@'localhost' IDENTIFIED BY 'scysqlspaces';
GRANT ALL PRIVILEGES ON `scysqlspaces`. * TO 'scysqlspaces'@'localhost';
