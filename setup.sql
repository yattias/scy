CREATE DATABASE IF NOT EXISTS `scyuseradmin` ;
CREATE USER 'scyuseradmin'@'localhost' IDENTIFIED BY 'scyuseradmin';
GRANT ALL PRIVILEGES ON `scyuseradmin`. * TO 'scyuseradmin'@'localhost';

CREATE DATABASE IF NOT EXISTS `sqlspaces` ;
CREATE USER 'sqlspaces'@'localhost' IDENTIFIED BY 'sqlspaces';
GRANT ALL PRIVILEGES ON `sqlspaces`. * TO 'sqlspaces'@'localhost';
