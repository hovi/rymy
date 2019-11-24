#!/bin/bash
set -eu
./gradlew build

rsync ./build/processedResources/jvm/main/index.html docs/
rsync ./build/distributions/rymy.js docs/
rsync ./build/processedResources/jvm/main/style.css docs/
