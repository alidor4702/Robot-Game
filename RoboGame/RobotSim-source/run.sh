#!/bin/sh
# Builds and launches the Robot Game. Needs Java 17+ and Maven.
cd "$(dirname "$0")" && mvn -q javafx:run
