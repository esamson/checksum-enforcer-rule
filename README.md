# Retired

As of the 3.0.0 release, this is now part of Maven Enforcer built-in rules as
[requireFileChecksum][2]. Use that, not this.

# Maven Enforcer Checksum Rule

This is a custom rule for the [maven-enforcer-plugin][1] to verify the checksum
of a file. To use in your project, add to your enforcer plugin configuration.
For example, to ensure that you have the correct Oracle JDBC 12.1.0.2 jars:

    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-enforcer-plugin</artifactId>
        <version>1.4</version>
        <dependencies>
            <dependency>
                <groupId>ph.samson.maven</groupId>
                <artifactId>checksum-enforcer-rule</artifactId>
                <version>0.0.1</version>
            </dependency>
        </dependencies>
        <executions>
            <execution>
                <id>file-checksums</id>
                <goals>
                    <goal>enforce</goal>
                </goals>
                <configuration>
                    <rules>
                        <fileChecksum implementation="ph.samson.maven.enforcer.rule.checksum.FileChecksum">
                            <file>${oracle.dir}/ojdbc7.jar</file>
                            <checksum>7c9b5984b2c1e32e7c8cf3331df77f31e89e24c2</checksum>
                        </fileChecksum>
                        <fileChecksum implementation="ph.samson.maven.enforcer.rule.checksum.FileChecksum">
                            <file>${oracle.dir}/ucp.jar</file>
                            <checksum>ef4f2f99b07ee26673be62bbdccce78b0209042c</checksum>
                        </fileChecksum>
                    </rules>
                </configuration>
            </execution>
        </executions>
    </plugin>

By default, SHA1 is used. Set the `type` parameter to specify a different
algorithm. Possible values are:

* "crc32"
* "crc32c"
* "md5"
* "sha1"
* "sha256"
* "sha512"

[1]: https://maven.apache.org/enforcer/maven-enforcer-plugin/
[2]: https://maven.apache.org/enforcer/enforcer-rules/requireFileChecksum.html
