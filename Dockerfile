FROM amd64/openjdk:14-buster
COPY build/app/gumtree-scraper.jar /opt/
CMD ["java", "--enable-preview", "-jar", "/opt/gumtree-scraper.jar"]
