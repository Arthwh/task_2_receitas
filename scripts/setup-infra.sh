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

# Cria container do SonarQube
sudo chmod +x ./cria-container-sonarqube.sh
sudo ./cria-container-sonarqube.sh

# Cria container do Jenkins
# Concede permissão de execução pro arquivo de configuração do jenkins
sudo chmod +x ./jenkins.yaml

#Cria a imagem do jenkins personalizada pelo Dockerfile
sudo chmod +x ./Dockerfile.jenkins
sudo docker build -t jenkins -f Dockerfile.jenkins .

#Inicia o container do Jenkins com a imagem criada
sudo chmod +x ./cria-container-jenkins.sh
sudo ./cria-container-jenkins.sh

#Mostra os logs do container do jenkins
sudo docker logs -f servidor-jenkins

echo "Log da pipeline Jenkins (Pressione CTRL+C para sair):"
sleep 5
sudo docker exec -it servidor-jenkins tail -f /var/jenkins_home/jobs/Configura-Repositorio-Jenkins/lastBuild/log