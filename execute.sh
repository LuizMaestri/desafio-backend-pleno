#!/usr/bin/env bash
mvn clean install && sudo docker-compose up --build -d
