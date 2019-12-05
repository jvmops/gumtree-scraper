## About
Polish private rental apartment market is established around gumtree. Ads are displayed based on creation time (newest first). The main issue here is that most of these ads are re-posted every day in order for them to show up on top of the list. From the user perspective it kind of sux because you need to get through many ads before you find the ones introduced today...

This app solves that problem. It scrap ads, watch for duplicated offers and gather those data. Daily report with interesting findings is distributed to the concerned parties via a gmail account.

## Building from source:
In order to build from source you need:
- JDK 13
- Maven
```
git clone https://github.com/jvmops/gumtree-scrapper.git
cd gumtree-scrapper
mvn package
```

## Running:
Runtime involves:
- Docker
- Mongo
- Gmail account (you should generate app password)

With a mongo connection URI you can run a container using an image from the docker hub. Start scrapping by typing:
```bash
docker run --name gumtree-scrapper \
  -e SPRING_PROFILES_ACTIVE=scrapper \
  -e SPRING_DATA_MONGODB_URI=mongodb://... \
    jvmops/gumtree-scrapper
```

In order for emails to be send run:
```bash
docker run --name gumtree-report \
  -e SPRING_PROFILES_ACTIVE=report \
  -e SPRING_DATA_MONGODB_URI=mongodb://... \
  -e SPRING_MAIL_USERNAME=... \
  -e SPRING_MAIL_PASSWORD=... \
    jvmops/gumtree-scrapper
```
