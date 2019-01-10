#!/bin/bash
set -ev
pwd
env
ls -al
ls -al ..
mkdir moditect-gradle-plugin
mv !(moditect-gradle-plugin) moditect-gradle-plugin
ls -al
git clone --depth 1 https://github.com/moditect/moditect.git moditect
# ./gradlew --no-daemon -i -s build
