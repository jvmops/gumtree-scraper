#!/usr/bin/env bash

project_version=$(./gradlew properties -q | grep "version:" | awk '{print $2}')

./gradlew build \
  && docker build -t "jvmops/gumtree-scrapper:latest" -t "jvmops/gumtree-scrapper:$project_version" .
