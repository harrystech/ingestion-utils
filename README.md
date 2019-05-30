# ingestion-utils
Common utilities used to implement hyppo integrations.

## Docker

This repo also contains some Docker-related resources that help local development of Hyppo integrations using Docker and Docker Compose.

### Build the Base Image

This is the base image used by both Hyppo Manager and the integrations you work on.

```
docker build -t hyppo-build docker/ 
```

### Next Steps

The `docker-compose.yaml` file sets up a working local Hyppo cluster. You use it by symlinking to that file from directory where the integration you're working on is stored.

To build Hyppo Manager, follow the instructions in [this manual](https://harrys.atlassian.net/wiki/spaces/DE/pages/279117905/Hyppo+Development+Environment+with+Docker).
