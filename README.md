# ingestion-utils
Common utilities used to implement hyppo integrations.

## Building Docker image

This repo also contains a Dockerfile to build an image that offers a JDK and sbt.

```
docker build -t scala-sbt .
```

Keep this image in your back-pocket for SBT projects.

## Using Docker container

```
docker run --rm --interactive --tty --volume `pwd`:/app scala-sbt
```

Once inside the container, use `sbt` as usual, _e.g._
```
clean
compile
```
