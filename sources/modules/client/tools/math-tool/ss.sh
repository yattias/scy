#!/bin/sh

mvn install
jarsigner -keystore .keystore -storepass mathtool -keypass mathtool ./lib/json-20070829.jar mathtool
jarsigner -keystore .keystore -storepass mathtool -keypass mathtool ./lib/jettison-1.2.jar mathtool 
jarsigner -keystore .keystore -storepass mathtool -keypass mathtool ./lib/jeval-0.9.4.jar mathtool 
jarsigner -keystore .keystore -storepass mathtool -keypass mathtool ./lib/swingx-1.6.1.jar mathtool 
jarsigner -keystore .keystore -storepass mathtool -keypass mathtool ./lib/filters-2.0.235.jar mathtool 
jarsigner -keystore .keystore -storepass mathtool -keypass mathtool ./lib/swing-worker-1.1.jar mathtool 
jarsigner -keystore .keystore -storepass mathtool -keypass mathtool ./lib/commons-lang-2.5.jar mathtool 
jarsigner -keystore .keystore -storepass mathtool -keypass mathtool ./lib/miglayout-3.7.3.1-swing.jar mathtool 
jarsigner -keystore .keystore -storepass mathtool -keypass mathtool ./lib/xstream-1.4-SNAPSHOT.jar mathtool 
jarsigner -keystore .keystore -storepass mathtool -keypass mathtool ./lib/xpp3_min-1.1.4c.jar mathtool 
jarsigner -keystore .keystore -storepass mathtool -keypass mathtool ./lib/commons-io-2.0.jar mathtool 
jarsigner -keystore .keystore -storepass mathtool -keypass mathtool ./target/math-tool-35-SNAPSHOT.jar mathtool 
