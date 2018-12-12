# ingestion-utils
Common utilities used to implement hyppo integrations.


# Docker

1. Build base build image
2. Build private dependency JARs, in this order:
    1. https://github.com/harrystech/scala-postgres-utils
    2. https://github.com/harrystech/scala-jooq-tables
    3. https://github.com/harrystech/orika-utils
3. Build "normal" build base, putting those JARs into `<root>/lib`
4. Build the Manager image

## Build base image

1. docker build -t harrystech/hyppo-base -f docker/Dockerfile .

## Build dependent JARs

1. cd $TARGET_REPO
2. cp $THIS_REPO/docker/Dockerfile $TARGET_REPO
3. docker build -t $TARGET_REPO .
4. docker run $TARGET_REPO  # Run once just to create the container
5. docker ps -a  # Get container id
6. docker cp $CONTAINER_ID:/app/target/scala-$SCALA_VERSION/$BUILT_JAR_NAME ./lib

Repeat above for each of the three dependent projects, putting all of the JARs into `lib/`.
