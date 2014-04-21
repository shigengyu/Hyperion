/////////////////////////////////////////////
//
//   Steps to setup Sonatype in Maven:
//
/////////////////////////////////////////////

1. Add the following section into Hyperion pom.xml

	<parent>
		<groupId>org.sonatype.oss</groupId>
		<artifactId>oss-parent</artifactId>
		<version>7</version>
	</parent>

	<scm>
		<connection>scm:git:git@github.com:shigengyu/hyperion.git</connection>
		<developerConnection>scm:git:git@github.com:shigengyu/hyperion.git</developerConnection>
		<url>git@github.com:shigengyu/hyperion.git</url>
	</scm>

2. Add the following section to settings.xml in Maven conf directory:

    <server>
      <id>sonatype-nexus-snapshots</id>
      <username>univer.shi@gmail.com</username>
      <password>[password]</password>
    </server>
    <server>
      <id>sonatype-nexus-staging</id>
      <username>univer.shi@gmail.com</username>
      <password>[password]</password>
    </server>

 * The password in Maven settings.xml can be plain text, or can be encrypted. To encrypt the password, follow the link below:
   http://maven.apache.org/guides/mini/guide-encryption.html
 