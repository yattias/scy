== DESCRIPTION ==

This folder contains the SCYLab including all tools.

== USAGE ==

* Debugging: In the "lab" subfolder use "mvn com.totsp.gwt:maven-googleweit2-plugin:gwt"
* Packaging: Running "mvn package" will produce lab/target/scy-lab.war
* Local Deployment: After running "mvn install tomcat:run-war" you can open http://localhost:8080/scy-lab

== TODO ==

* Fix Drawing Tool: include source code/generated jars
* Refactor eu.scy.global
* Include the monitor component
* Inherit from SCY super pom
* Add resource filtering to get the version number in SCYLab

* Reduce gwt-ext?

* KNOWN BUG: Naming prefix conflict between used gwt-maven plugin and another Codehaus plugin.
	See http://code.google.com/p/gwt-maven/issues/detail?id=107
* KNOWN BUG: The maven gwt plugin complains about modules without entry points.
	May be ignored in this set up. See http://code.google.com/p/gwt-maven/issues/detail?id=28 
* KNOWN BUG: ${settings.localRespository} does not get expanded.
	Maven2 bug which will hopefully get fixed soon. See http://jira.codehaus.org/browse/MNGECLIPSE-460
