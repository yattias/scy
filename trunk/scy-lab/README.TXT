== DESCRIPTION ==

This folder contains the SCYLab including all tools.

== USAGE ==

Running "mvn package" will produce lab/target/scy-lab.war

== DEVELOPMENT ==

* Adding a new Tool:

* Compiling for gwt:

	<build>
		<plugins>
			<!--  configure the GWT-Maven plugin -->
			<plugin>
				<groupId>com.totsp.gwt</groupId>
				<artifactId>maven-googlewebtoolkit2-plugin
				</artifactId>
				<version>2.0-beta26</version>
				<executions>
					<execution>
						<goals>
							<!-- <goal>mergewebxml</goal> -->
							<!-- <goal>i18n</goal>-->
							<goal>compile</goal>
							<!-- <goal>test</goal> -->
						</goals>
					</execution>
				</executions>
			</plugin>
		</plugins>
	</build>

== TODO ==

* Fix Drawing Tool: include source code/generated jars
* Refactor eu.scy.global
* Include the monitor component
* Inherit from SCY super pom
* Add resource filtering to get the version number in SCYLab

* Reduce gwt-ext?

* KNOWN BUG: The maven gwt plugin complains about modules without entry points.
	May be ignored in this set up. See http://code.google.com/p/gwt-maven/issues/detail?id=28 
* KNOWN BUG: ${settings.localRespository} does not get expanded.
	Maven2 bug which will hopefully get fixed soon. See http://jira.codehaus.org/browse/MNGECLIPSE-460
