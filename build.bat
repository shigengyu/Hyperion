cd /d %~dp0
mvn package -Ponly-library

mvn clean install