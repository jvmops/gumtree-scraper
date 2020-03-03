FROM adoptopenjdk:13-jre-hotspot
COPY app/gumtree-scrapper.jar /opt/
CMD ["java", "-jar", "/opt/gumtree-scrapper.jar"]