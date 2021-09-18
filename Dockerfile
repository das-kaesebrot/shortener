# FROM tomcat:10-jre11
FROM tomcat

RUN rm -rf /usr/local/tomcat/webapps/ROOT
COPY ./build/libs/shortener-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
# CMD ["catalina.sh","run"]
