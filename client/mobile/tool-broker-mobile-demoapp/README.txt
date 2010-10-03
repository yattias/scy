To run:

1. Install Sun Java Wireless Toolkit for CLDC (version 2.5.2) from http://java.sun.com/products/sjwtoolkit/
2. Generate client stubs for web service. This is how you do it:
    2.1. Edit client-stubs.xml and repace the <wsdl location= .. attribute with an WSDL URL of a running webservice
    2.2. Edit the generate-stubs.bat file and replace the path to wscompile to your local WTK path
    2.3. Run generate-stubs.bat (or a command equivalent to it on other platforms)

3. Run the app in the emulator with the maven command:

    mvn -Dj2me.sdkPath=/path/to/your/WTK_HOME clean compile j2me:package j2me:run

4. Or this to create the jar/jad only
    mvn -Dj2me.sdkPath=/path/to/your/WTK_HOME clean compile j2me:package