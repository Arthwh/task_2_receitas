#!/bin/bash

# Cria um container com a imagem do SonarQube
sudo docker run -d \
  --name sonarqube \
  --restart always \
  -p 9000:9000 \
  sonarqube:lts-community