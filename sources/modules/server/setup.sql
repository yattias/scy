CREATE DATABASE IF NOT EXISTS `scy` ;
CREATE USER 'scyuseradmin'@'localhost' IDENTIFIED BY 'scyuseradmin';
GRANT ALL PRIVILEGES ON `scy`. * TO 'scyuseradmin'@'localhost';

CREATE DATABASE IF NOT EXISTS `sqlspaces` ;
CREATE USER 'sqlspaces'@'localhost' IDENTIFIED BY 'sqlspaces';
GRANT ALL PRIVILEGES ON `sqlspaces`. * TO 'sqlspaces'@'localhost';
