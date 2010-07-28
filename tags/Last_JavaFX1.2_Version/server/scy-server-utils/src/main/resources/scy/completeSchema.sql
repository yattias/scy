set FOREIGN_KEY_CHECKS=0;

set character_set_connection=utf8;
set character_set_results=utf8;
set character_set_client=utf8;

\. saildb.sql

INSERT INTO `granted_authorities` (`id`, `authority`, `OPTLOCK`) VALUES (1,'ROLE_USER',0),(2,'ROLE_ADMINISTRATOR',0),(3,'ROLE_TEACHER',0),(4,'ROLE_STUDENT',0),(5,'ROLE_AUTHOR',0);

\. upgradeDb.sql

\. dbSchema.sql

set FOREIGN_KEY_CHECKS=1;