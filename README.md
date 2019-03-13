# ingestion-utils
Common utilities used to implement hyppo integrations.

# Docker

## 1. Build base image

1. `docker build -t hyppo-build docker/.`

## 2. Build dependency JARs

1. Clone each of these dependency repos, and build in this order:
  * https://github.com/harrystech/scala-postgres-utils
    `git clone git@github.com:harrystech/scala-postgres-utils.git`
  * https://github.com/harrystech/scala-jooq-tables
    `git clone git@github.com:harrystech/scala-jooq-tables.git`
  * https://github.com/harrystech/orika-utils
    `git clone git@github.com:harrystech/orika-utils.git`
2. Run `make build-dependencies` in `ingestion-utils`

## 3. Build Hyppo Manager

1. Clone the Hyppo Manager repo:
  * https://github.com/harrystech/hyppo-manager
    `git clone git@github.com:harrystech/hyppo-manager.git`
2. Fill in all the `*.template` files with valid values and remove the .template extensions.
3. Run `make build-hyppo-manager` in `ingestion-utils`
4. cd to the integration
5. `docker-compose up`
