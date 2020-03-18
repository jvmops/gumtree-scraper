FROM amd64/openjdk:14-buster
COPY build/app/gumtree-scrapper.jar /opt/
CMD ["java", "-jar", "/opt/gumtree-scrapper.jar"]
