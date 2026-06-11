#!/bin/bash
# Cria um container com a imagem do Jenkins, e mapeia o arquivo de soquete do Docker para dentro do container
sudo docker run -d \
  --name servidor-jenkins \
  --user root \
  --restart always \
  -p 9001:8080 -p 50000:50000 \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -u root \
  jenkins

# -d executa o comando em segundo plano (detached)
# --name define um nome para o container
# -p mapeia a porta externa da VM para a porta do container <porta da VM>:<porta do container>
# -v cria um volume (ligação entre uma pasta do container e uma da VM), para salvar dados e não perder ao reiniciar o container (ele é volátil)
# -u define o usuário