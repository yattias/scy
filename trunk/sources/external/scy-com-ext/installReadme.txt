How to configure liferay portal at unix destination:


Important folder and files to backup!!! (liferay db and data folder from liferay ../CATALINA_HOME/ data)



tomcat:

1. install tomcat (all function only run on tomcat 6.0.18 !!!)
2. edit catalina.properties (change common.loader to):
		common.loader=${catalina.home}/lib,${catalina.home}/lib/*.jar,${catalina.home}/lib/ext/*.jar
3. change files at /CATALINA_HOME/lib/ext after "ant deploy" at scy-com-ext:
		 overwrite portal-kernel.jar and portal-service.jar with own copies of this files

database:

1. create db lportal
2. grant user privileges on db
3. create tables for own extension portlets from scy-com-ext/db.txt after run liferay at tomcat first time

 

liferay:

1. Create folder for svn files
2. Check out scy-com-Plugins_SDK, scy-com-ext, scy-elo-portlet
3. Edit properties files (/scy-com-ext/):
		app.server.User.properties: change User to name of local user with rights; app.server.tomcat.dir to tomcat dir
		build.User.properties: change User to name of local user with rights
4. Edit properties files (/scy-com-ext/ext-impl/src/):
		portal.ext.properties:
								- change db config (jdbc.default.driverClassName ...)
								- change cache setting for production		
5. "ant deploy" at scy-com-ext (install main lf with ext extensions)

Maven Elo portlet:

1. Download maven-sdk from Site http://wiki.github.com/azzazzel/liferay-maven-sdk/download-and-install
2. Unrar files to local dir
3  Execute „mvn install“ at this directory (Important only install will work not package !!!)
4. "mvn clean package" at destination "scy-elo-portlet"
5. move/cp dest war to liferay deploy folder (from tomcat as parent the folder is ../deploy)
6. wait for autodeploy from liferay if tomcat server is started


extended liferay community portlet (at moment chat-portlet and wol-portlet):

1. Change build.user.properties file name to build."username of local system".properties (scy-com-Plugins_SDK)
2. Change app.server.dir at property file to tomcat destination folder
3. "ant deploy" at /scy-com-Plugins_SDK/portlets/wol-portlet

Liferay scy theme:

1. If step 1+2 is not done from "extended liferay ..." then done yet
2. "ant deploy" at /scy-com-Plugins_SDK/themes/scy-theme
3. wait for autodeploy


Chat potlet:

1. goto admin - control panel - plugin installtion
2. "install more"
3. search "chat"
4. "Chat (v5.2.2.1)" install
5. change portlet properties at /tomcat/webapps/chat-portlet/WEB-INF/classes/portlet.properties file to buddy.list.strategy=friends

