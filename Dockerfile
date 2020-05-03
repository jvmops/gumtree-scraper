FROM amd64/openjdk:14-buster
COPY build/app/gumtree-scrapper.jar /opt/
CMD ["java", "--enable-preview", "-jar", "/opt/gumtree-scrapper.jar"]
