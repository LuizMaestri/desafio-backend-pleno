#!/usr/bin/env bash
mvn clean install && sudo docker-compose up --build -d && sudo docker-compose logs --no-color -f > log.txt &
