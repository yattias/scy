
Only add libraries that cannot be found in any maven repositories to this folder!

Add a filesystem library to pom.xml with:

    <dependency>
        <groupId>org.the.group</groupId>
        <artifactId>the-artifact</artifactId>
        <classifier>jar</classifier>
        <version>1.0</version>
        <scope>system</scope>
        <systemPath>${basedir}\lib\the-jar.jar</systemPath>
    </dependency>

NOTE: system and provided dependencies will not be packaged unless explicitly stated in the
assembly area of the j2me-maven-plugin configuration in pom.xml. To instruct the j2me-maven-plugin
to include provided or system dependencies when packaging, add an inclusion to the assembly part
of the configuration part of the j2me-maven-plugin section in pom.xml like this:
 
    <assembly>
        <inclusions>
            <inclusion>
                <groupId>org.json.me</groupId>
                <artifactId>json</artifactId>
                <classifier>jar</classifier>
            </inclusion>
            <inclusion>
                <groupId>com.sun.me.web</groupId>
                <artifactId>request</artifactId>
                <classifier>jar</classifier>
            </inclusion>
        </inclusions>
    </assembly>