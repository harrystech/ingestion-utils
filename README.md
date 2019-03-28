# ingestion-utils
Common utilities used to implement hyppo integrations.

# Docker

Optionally, clean out your docker images by stopping running containers and running:
```docker rmi $(docker images -q)
cd ingestion-utils
docker build -t hyppo-build docker/.
```

To build Hyppo Manager, follow the instructions in [this manual](https://harrys.atlassian.net/wiki/spaces/DE/pages/279117905/Hyppo+Development+Environment+with+Docker).
