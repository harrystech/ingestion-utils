# ingestion-utils
Common utilities used to implement hyppo integrations.


# Docker

## 1. Build dependency JARs

Clone each of these dependency repos, and build in this order:
1. https://github.com/harrystech/scala-postgres-utils
2. https://github.com/harrystech/scala-jooq-tables
3. https://github.com/harrystech/orika-utils

For each repo:
1. cd $TARGET_REPO
2. docker build -t $TARGET_REPO -f $INGESTION_UTILS_REPO/docker/Dockerfile .
3. docker create --name build $TARGET_REPO  # Lets us get the JAR out
4. docker cp build:/app/target/scala-2.11/$BUILT_JAR_NAME /tmp  # Get JAR out of container

Repeat above for each of the three dependent projects, collecting the compiled JARs into some temporary directory you can easily find and access later.

## 2. Build Hyppo Manager

1. Clone the Hyppo Manager repo:
    * https://github.com/harrystech/hyppo-manager
2. cd hyppo-manager
3. cp /tmp/scala-jooq-tables*.jar ./lib
4. docker build -t hyppo-manager -f $INGESTION_UTILS_REPO/docker/Dockerfile .
