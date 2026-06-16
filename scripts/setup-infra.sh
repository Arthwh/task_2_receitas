#!/bin/bash
# "Shebang" diz para o linux que o arquivo deve ser interpretado como shell

# Atualiza os pacotes
sudo apt update && apt upgrade -y

# Instala o git
sudo apt install git -y

# Instala o docker
sudo chmod +x ./instala-docker.sh
sudo ./instala-docker.sh

# Adiciona o usuário atual no grupo de usuários do docker
sudo usermod -aG docker $USER

# ---------- Cria container do SonarQube (COMENTADO POIS A VM NAO TEM DESEMPENHO PARA RODAR TUDO) -----------
#sudo chmod +x ./cria-container-sonarqube.sh
#sudo ./cria-container-sonarqube.sh

# Concede permissão de leitura para os arquivos .env
sudo chmod 644 ./secrets/*.env

#--------- Cria container do Jenkins -----------
# Concede permissão de execução pro arquivo de configuração do jenkins
sudo chmod +x ./jenkins.yaml

# Cria a imagem do jenkins pelo Dockerfile
sudo chmod +x ./Dockerfile.jenkins
sudo docker build -t jenkins -f Dockerfile.jenkins .

# Inicia o container do Jenkins com a imagem criada
sudo chmod +x ./cria-container-jenkins.sh
sudo ./cria-container-jenkins.sh

# Espera 30s até o container do Jenkins estar pronto
sudo sleep 30

# Mostra os logs do container do jenkins
sudo docker logs --tail 50 servidor-jenkins

# Executa a pipeline
sudo chmod +x ./start-jenkins-job.sh
sudo ./start-jenkins-job.sh