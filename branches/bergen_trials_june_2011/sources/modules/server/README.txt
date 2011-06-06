== DESCRIPTION ==

This directory contains the server for the SCY Project.

== USAGE ==

1. Set up MySQl (you could use setup.sql in this directory)
	* Create a user who has all rights on the database scy (for SCY user management)
	* Create a user who has all rights on the database sqlspaces (for SQLSpaces)

2. Configure the SCY server
	* Use the template at "scy-useradmin-impl-src/test/resources/sample_filter_rename_me.properties"
	* Create the following files with the usernames and passwords for the two users you created in step 1.
		* scy-useradmin-impl-src/test/resources/filter.properties
		* scy-useradmin-web/src/main/filters/filter.properties

3. Run the SCY-Server
	* In the "scy-useradmin-web" subdir execute "mvn jetty:run"
	* Open http://localhost:8080/scy-useradmin-web
