#! /bin/bash

. ~/.artifactory/config

cat ../hyppo-manager/dotfiles/harrys-artifactory.credentials.template | sed -e "s/user=/user=$ARTIFACTORY_USERNAME/" | sed -e "s/password=/password=$ARTIFACTORY_PASSWORD/" > ../hyppo-manager/dotfiles/harrys-artifactory.credentials


