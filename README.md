# gumtree-scrapper
Scraps info about apartments in Wroclaw and notifies you through email about interesting findings

## running:

```bash
docker run --name mongo -d -p 27017:27017 mongo:3.4
```

## Scheduler
In order to run scheduler start app with profile `scheduler`