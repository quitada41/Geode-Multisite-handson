#! /bin/sh

gfsh start server --name=c1s2 --dir=server2 --server-port=0 --properties-file=geode.properties --cache-xml-file=cache-wan.xml
