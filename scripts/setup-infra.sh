#!/bin/bash
# "Shebang" diz para o linux que o arquivo deve ser interpretado como shell

# Atualiza os pacotes
sudo apt update && apt upgrade -y

# Instala os pacotes necessários para o início da configuração
sudo apt install docker.io docker-compose git -y

# Adiciona o usuário atual no grupo de usuários do docker (tem permissão para manipular, sem ser "sudo")
sudo usermod -aG docker $USER

# Cria um container com a imagem do Jenkins, e mapeia o arquivo de soquete do Docker para dentro do container
sudo docker run -d \
  --name jenkins \
  -p 9001:9001 -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -u root \
  jenkins/jenkins:lts

# -d executa o comando em segundo plano (detached)
# --name define um nome para o container
# -p mapeia a porta externa da VM para a porta do container <porta da VM>:<porta do container>
# -v cria um volume (ligação entre uma pasta do container e uma da VM), para salvar dados e não perder ao reiniciar o container (ele é volátil)
# -u define o usuário

# Cria um container com a imagem do SonarQube
sudo docker run -d \
  --name sonarqube \
  -p 9000:9000 \
  sonarqube:lts-community