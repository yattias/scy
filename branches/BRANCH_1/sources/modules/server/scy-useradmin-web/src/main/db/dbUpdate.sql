set FOREIGN_KEY_CHECKS=0;

set character_set_connection=utf8;
set character_set_results=utf8;
set character_set_client=utf8;

-- Used to drop database and create it again
\. dropAndCreate.sql

-- Default bite d schema
\. scy-db-structure.sql

-- SAIL SCHEMA

\. sail-db-structure.sql

set FOREIGN_KEY_CHECKS=1;