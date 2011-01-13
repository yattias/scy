#!/bin/bash
echo "Downloading JavaFX libs"
for i in `egrep "href=\"([^\"]*.jar)" javafx-libs/javafx-rt.jnlp -o | cut -b7-`
	do
	echo "Downloading $i from Oracle"
	wget http://dl.javafx.com/1.3/$i -O javafx-libs/$i
done
jar -cvf javafx-libs.war javafx-libs/*
echo "javafx-libs.war file created, now copy it to your webapps folder of tomcat."
echo "Did you remember to change the codebase inside the JNLP file to fit your server?"
