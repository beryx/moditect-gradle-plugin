#!/bin/bash
set -ev
pwd
env
ls -al
ls -al ..
mkdir moditect-gradle-plugin
find . -maxdepth 1 -not -name . -not -name moditect-gradle-plugin | xargs -i mv {} ./moditect-gradle-plugin
ls -al
git clone --depth 1 https://github.com/moditect/moditect.git moditect
ls -al
# ./gradlew --no-daemon -i -s build
