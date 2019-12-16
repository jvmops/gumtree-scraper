<h1 align="center">
  gumtree-scrapper<br>
  <a href="https://github.com/jvmops/gumtree-scrapper/actions"><img align="center" src="https://github.com/jvmops/gumtree-scrapper/workflows/build/badge.svg"></a> 
  <a href="https://codecov.io/gh/jvmops/gumtree-scrapper"><img align="center" src="https://codecov.io/gh/jvmops/gumtree-scrapper/branch/master/graph/badge.svg"></a> 
  <a href="https://github.com/jvmops/gumtree-scrapper/blob/master/LICENSE"><img align="center" src="https://img.shields.io/github/license/jvmops/gumtree-scrapper.svg"></a>
  <br><br>
</h1>

Polish private rental apartment market is established around gumtree. Ads are displayed based on creation time (newest first). The main issue here is that most of these ads are re-posted every day in order for them to show up on top of the list. From the user perspective it kind of sux because you need to get through many ads before you find the ones introduced today...

This app solves that problem. It scrap ads, watch for duplicated offers and gather those data. Daily report with interesting findings is distributed to the concerned parties via a gmail account.

## Building from source:
In order to build from source you need:
- JDK 13
- Docker (integration tests dependency)
```
git clone https://github.com/jvmops/gumtree-scrapper.git
cd gumtree-scrapper
./mvnw test
```

## Running:
Runtime involves:
- docker-compose
- Gmail account (you should generate app password)

Adjust the `.env` file and run:
```bash
docker-compose up
```
You can now open an admin page in the browser: http://localhost:8080/cities  
To run a scrap job: `docker start gumtree-scrapper`  
To send reports: `docker start gumtree-report`

## Roadmap
[github issues](https://github.com/jvmops/gumtree-scrapper/projects)
