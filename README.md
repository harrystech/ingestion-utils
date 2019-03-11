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

Run `make build-dependencies`

## 3. Build Hyppo Manager

1. Clone the Hyppo Manager repo:
    * https://github.com/harrystech/hyppo-manager
2. Write a config file to `~/.artifactory/config` using the template below. The user is your Github handle. To populate your password, visit `https://harrys.jfrog.io/harrys/webapp/#/login` and log in with yout Github creds. Once in, access your profile settings (top right of page) and copy your API key. Use the API key as your password.
3. (from ingestion-utils) make `build-hyppo-manager`

```
user=user
password=
```

## 4. Configure and run Manager & Worker

1. Clone repo for the Hyppo integration you want to work on. You launch the Hyppo cluster from the root of the specific integration project you're working on.
2. `cd $TARGET_REPO`
3. `make setup-$TARGET-REPO`
7. Run `docker-compose up`
12. Run docker-compose run build.
13. Once you are dropped into the shell, run `sbt assembly`. Once this is done, check the logs to see where the JAR file is written and use `aws s3 cp` to move it to `S3` per the instructions in the integration README.
