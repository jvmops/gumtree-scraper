FROM adoptopenjdk:13-jre-hotspot
COPY build/libs/gumtree-scrapper.jar /opt/
CMD ["java", "-jar", "/opt/gumtree-scrapper.jar"]