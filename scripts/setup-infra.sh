#!/bin/bash
# "Shebang" diz para o linux que o arquivo deve ser interpretado como shell

# Atualiza os pacotes
sudo apt update && apt upgrade -y

# Instala os pacotes necessários para o início da configuração
sudo apt install git -y

# Instala o docker a partir de outro script
sudo chmod +x ./instala-docker.sh
sudo ./instala-docker.sh

# Adiciona o usuário atual no grupo de usuários do docker (tem permissão para manipular, sem ser "sudo")
sudo usermod -aG docker $USER

# Cria container do Jenkins
#sudo chmod +x ./cria-container-jenkins.sh
sudo chmod +x ./Dockerfile.jenkins
sudo docker build -t jenkins -f Dockerfile.jenkins .
#sudo ./cria-container-jenkins.sh

# Cria container do SonarQube
sudo chmod +x ./cria-container-sonarqube.sh
sudo ./cria-container-sonarqube.sh

# Clona o repositorio do github
#sudo chmod +x ./clona-repositorio-github.sh
#sudo ./clona-repositorio-github.sh