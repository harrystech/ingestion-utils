# ingestion-utils
Common utilities used to implement hyppo integrations.


# Docker

## 1. Build base image

1. `cd docker`
2. `docker build -t hyppo-build .`

## 2. Build dependency JARs

Clone each of these dependency repos, and build in this order:
1. https://github.com/harrystech/scala-postgres-utils
2. https://github.com/harrystech/scala-jooq-tables
3. https://github.com/harrystech/orika-utils

Run
`make build-dependencies`

## 3. Build Hyppo Manager

1. Clone the Hyppo Manager repo:
    * https://github.com/harrystech/hyppo-manager
2. `cd hyppo-manager`
3. `mkdir lib && cp /tmp/scala-jooq-tables*.jar lib/`
4. `docker build -t hyppo-manager .`

## 4. Configure and run Manager & Worker

1. Clone repo for the Hyppo integration you want to work on. You launch the Hyppo cluster from the root of the specific integration project you're working on.
2. `cd $TARGET_REPO`
3. Copy jars needed from prior dependencies
  `mkdir lib && cp /tmp/*.jar lib/`
4. `cp $INGESTION_UTILS_REPO/docker/\*.template $TARGET_REPO/`
5. `ln -s $INGESTION_UTILS_REPO/docker/docker-compose.yaml`
6. Fill in all the `*.template` files with valid values and remove the `.template` extensions.
7. `docker-compose up`

## 5. Build an integration project

1. Go to the integration project you want to work on (assuming cloned and configured previously).
2. `cd $TARGET_REPO`
3. `cp $INGESTION_UTILS_REPO/docker/\*.template $TARGET_REPO/`
4. `ln -s $INGESTION_UTILS_REPO/docker/docker-compose.yaml`
5. Fill in all the `*.template` files with valid values and remove the `.template` extensions.
6. Copy in all the dependency JARs built earlier. For each one:
    ```
    $ cp /tmp/$BUILT_JAR_NAME lib/
    ```
7. `docker-compose run build`
8. `sbt`
9. `docker-compose stop`
