== DESCRIPTION ==

This folder contains the SCYLab including all tools.

== USAGE ==

* Packaging: Running "mvn package" will produce lab/target/scy-lab.war
* Local Deployment: After running "mvn install tomcat:run-war" you can open http://localhost:8080/scy-lab
* Debugging: In the "lab" subfolder use "mvn com.totsp.gwt:maven-googlewebtoolkit2-plugin:gwt"

IMPORTANT: 
	Because of a bug in a used library (gwt-ai) you have to run maven with java 1.5  !
	Otherwise you will get a StackOverflowError, see http://code.google.com/p/gwtai/issues/detail?id=4

	Furthermore the SimQuestViewer applet will not be loaded, unless the jar is unpacked.
	Thus some manual work is necessary, to unpack the simquestviewer-all.jar in the final war.

== TODO ==

* Refactor eu.scy.global: Rename to scy-lab-api?
* Include the monitor component
* Inherit from SCY super pom
* Add resource filtering to get the version number in SCYLab
* Split tools into server and client parts?
	* Server side dependency should be "runtime" or "compile", while client side is "provided" -> Only classed needed for Servlets end up in the war 

* KNOWN BUG: Naming prefix conflict between used gwt-maven plugin and another Codehaus plugin.
	See http://code.google.com/p/gwt-maven/issues/detail?id=107
* KNOWN BUG: The maven gwt plugin complains about modules without entry points.
	May be ignored in this set up. See http://code.google.com/p/gwt-maven/issues/detail?id=28 
* KNOWN BUG: ${settings.localRespository} does not get expanded.
	Maven2 bug which will hopefully get fixed soon. See http://jira.codehaus.org/browse/MNGECLIPSE-460
