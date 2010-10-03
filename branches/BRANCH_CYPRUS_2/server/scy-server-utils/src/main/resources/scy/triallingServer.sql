\. dropandcreatesaildatabase.sql

set FOREIGN_KEY_CHECKS=0;

set character_set_connection=utf8;
set character_set_results=utf8;
set character_set_client=utf8;

\. saildb.sql

\. upgradeDb.sql

\. dbSchema.sql

set FOREIGN_KEY_CHECKS=0;

\. preTriallingDataInsert.sql
\. filteredProductionData.sql
\. postTriallingDataInsert.sql

set FOREIGN_KEY_CHECKS=1;