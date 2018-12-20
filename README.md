# ingestion-utils
Common utilities used to implement hyppo integrations.


# Docker

## 1. Build base image

1. cd docker
2. docker build -t hyppo-build .

## 2. Build dependency JARs

Clone each of these dependency repos, and build in this order:
1. https://github.com/harrystech/scala-postgres-utils
2. https://github.com/harrystech/scala-jooq-tables
3. https://github.com/harrystech/orika-utils

For each repo:
1. cd $TARGET_REPO
    * scala-jooq-tables depends on scala-postgres-utils, therefore:
    * mkdir lib && cp /tmp/scala-postgres-utils_*.jar lib/
2. docker build -t $TARGET_REPO .
3. docker create --name build $TARGET_REPO  # Lets us get the JAR out
4. docker cp build:/app/target/scala-2.11/$BUILT_JAR_NAME /tmp  # Get JAR out of container
5. docker rm build

Repeat above for each of the three dependent projects, collecting the compiled JARs into some temporary directory you can easily find and access later.

## 3. Build Hyppo Manager

1. Clone the Hyppo Manager repo:
    * https://github.com/harrystech/hyppo-manager
2. cd hyppo-manager
3. mkdir lib && cp /tmp/scala-jooq-tables*.jar lib/
4. docker build -t hyppo-manager .

## 4. Configure and run Manager & Worker

1. Clone repo for the Hyppo integration you want to work on.
2. cd $TARGET_REPO
3. cp $INGESTION_UTILS_REPO/docker/\*.template $TARGET_REPO/
4. ln -s $INGESTION_UTILS_REPO/docker/docker-compose.yaml
5. Fill in all the `*.template` files with valid values and remove the `.template` extensions.
6. docker-compose up

## 5. Build an integration project

1. Clone the ingestion project repo
2. cd $TARGET_REPO
3. cp $INGESTION_UTILS_REPO/docker/\*.template $TARGET_REPO/
4. ln -s $INGESTION_UTILS_REPO/docker/docker-compose.yaml
5. Fill in all the `*.template` files with valid values and remove the `.template` extensions.
6. Copy in all the dependency JARs built earlier. For each one:
    ```
    $ cp /tmp/$BUILT_JAR_NAME lib/
    ```
7. docker-compose run build
8. sbt
9. docker-compose stop
