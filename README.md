####Billing Solution:
=====================
 * Billing Webapp
 * Billing Persist
 
####Maven archetype
===================
* Maven Webapp archetype

```
mvn archetype:generate \
-DarchetypeArtifactId=maven-archetype-webapp \
-DarchetypeVersion=LATEST
``` 

* Vaadin Application archetype

``` 
 mvn archetype:generate \
   -DarchetypeGroupId=com.vaadin \
   -DarchetypeArtifactId=vaadin-archetype-application \
   -DarchetypeVersion=LATEST \
   -DgroupId=in.retalemine \
   -DartifactId=vaadin-app \
   -Dversion=1.0 \
   -Dpackaging=war
``` 

####Maven commands:
===================
 * mvn eclipse:clean
 * mvn eclispe:eclipse __converts to eclipse java project__
 * mvn eclipse:eclipse -Dwtpversion=2.0 __converts to eclipse web project__
 * mvn clean
 * mvn compile
 * mvn compile -U __force update__
 * mvn compile -Dmaven.test.skip=true __skips compilation and execution__ 
 * mvn compile -DskipTests=true __skips execution__
 * mvn test-compile
 * mvn compiler:testCompile
 * mvn test
 * mvn surefire:test
 * mvn install
 * mvn package
 * mvn exec:java
 * mvn exec:java -Dexec.mainClass="in.retalemine.App"
 * mvn exec:java -Dexec.mainClass="in.retalemine.App" -Dexec.args="'first arg'"
 * mvn spring-boot:run
 * mvn jetty:run  __scattered mode__
 * mvn jetty:run-war  __packaged mode__
 * mvn -Djetty.port=8181 jetty:run __enables to run the app in another port if 8080 is occupied__
 * mvn jetty:stop
