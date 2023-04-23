FROM khipu/openjdk17-alpine
# who is the author
MAINTAINER richard

# WORKDIR /usr

# make a new directory to store the jdk files

ENV TZ Asia/Tokyo

ADD indoor-positioning-system.jar /app/indoor-positioning-system.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/indoor-positioning-system.jar"]