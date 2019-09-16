#!/usr/bin/env bash
mvn clean install && docker-compose up --build -d && docker-compose logs --no-color -f > log.txt &