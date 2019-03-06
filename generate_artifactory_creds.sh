#! /bin/bash

. ~/.artifactory/config

cat ../hyppo-manager/dotfiles/harrys-artifactory.credentials.template | sed -e "s/user=/user=$user/" | sed -e "s/password=/password=$password/" > ../hyppo-manager/dotfiles/harrys-artifactory.credentials


