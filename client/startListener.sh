#!/bin/sh
java -classpath ${GEODE_HOME}/lib/geode-dependencies.jar:./lib/GeodeCacheListenerClient.jar io.pivotal.akitada.GeodeCacheListenerClient $1 $2 $3
