FROM tomcat:9-jre8
ADD billing-webapp/target/retalemine-v1.war /usr/local/tomcat/webapps/

# docker run -it --rm -p 8888:8080 -v /myspace/codebase-online/github/retalemine/retalemine-v1/billing-webapp/target/retalemine-v1.war:/usr/local/tomcat/webapps/retalemine-v1.war --name tomcat-docker tomcat:9-jre8
# docker run -it --rm -p 8080:8080 -v /myspace/codebase-online/github/retalemine/retalemine-v1/billing-webapp/target:/usr/local/tomcat/webapps/ --name tomcat-docker tomcat:9-jre8
# docker run -d -p 27017:27017 --name mongod-docker mongo:4
# docker run -it --rm -p 8888:8080 -v /myspace/codebase-online/github/retalemine/retalemine-v1/billing-webapp/target/retalemine-v1.war:/usr/local/tomcat/webapps/retalemine-v1.war --link mongod-docker:mongodb --name tomcat-docker tomcat:9-jre8
