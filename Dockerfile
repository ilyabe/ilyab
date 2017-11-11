FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/ilyab.jar /ilyab/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/ilyab/app.jar"]
