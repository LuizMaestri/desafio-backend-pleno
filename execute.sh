#!/usr/bin/env bash
mvn clean install && sudo docker-compose -f docker/docker-compose.linux.yml up --build -d
