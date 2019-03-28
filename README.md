# ingestion-utils
Common utilities used to implement hyppo integrations.

# Build a base image for Hyppo development

Optionally, clean out your docker images by stopping running containers and running:
```docker rmi $(docker images -q)```

From `ingestion-utils`, run:
```docker build -t hyppo-build docker/. ```

# Next steps

To build Hyppo Manager, follow the instructions in [this manual](https://harrys.atlassian.net/wiki/spaces/DE/pages/279117905/Hyppo+Development+Environment+with+Docker).
