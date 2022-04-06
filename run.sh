#!/bin/bash

[ -z $MONGO_URI ]  && (echo "Set \$MONGO_URI env variable with the MongoDB server URI" && exit)

npx nodemon --signal SIGINT -x mvn tomcat7:run