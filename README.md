<h1 align="center">
  gumtree-scrapper<br>
  <a href="https://github.com/jvmops/gumtree-scrapper/actions"><img align="center" src="https://github.com/jvmops/gumtree-scrapper/workflows/master/badge.svg"></a> 
  <a href="https://codecov.io/gh/jvmops/gumtree-scrapper"><img align="center" src="https://codecov.io/gh/jvmops/gumtree-scrapper/branch/master/graph/badge.svg"></a> 
  <a href="https://github.com/jvmops/gumtree-scrapper/blob/master/LICENSE"><img align="center" src="https://img.shields.io/github/license/jvmops/gumtree-scrapper.svg"></a>
  <br><br>
</h1>

Polish private rental apartment market is established around gumtree. Ads are displayed based on creation time (newest first). The main issue here is that most of these ads are re-posted every day in order for them to show up on top of the list. From the user perspective it kind of sux because you need to get through many ads before you find the ones introduced today...

This app solves that problem. It scrap ads, watch for duplicated offers and gather those data. Daily report with interesting findings is distributed to the concerned parties via a gmail account.

## Building from sources:
To build and run this app you need:
- JDK 14
- docker
- docker-compose

```
git clone https://github.com/jvmops/gumtree-scrapper.git
cd gumtree-scrapper
./gradlew build
docker build -t jvmops/gumtree-scrapper .
```

## Running 
Just spin it up
```
docker-compose up -d
```

Website should be available at: http://localhost:8080/cities

## Running from a docker registry
Docker image is available at [docker hub](https://hub.docker.com/r/jvmops/gumtree-scrapper)
```
docker pull jvmops/gumtree-scrapper
```
