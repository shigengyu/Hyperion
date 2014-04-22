/////////////////////////////////////////////
//
//   Steps to setup Sonatype in Maven:
//
/////////////////////////////////////////////

1. Add required information to Hyperion pom.xml
   http://central.sonatype.org/pages/requirements.html

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
 