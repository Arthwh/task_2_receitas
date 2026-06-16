#!/bin/bash

JENKINS_CONTAINER="servidor-jenkins"
JENKINS_USER="admin"
JENKINS_URL="localhost:9001"
JENKINS_JOB="Registro-Receitas-Deploy-Pipeline"

# Tenta pegar a initialAdminPassword
JENKINS_PASS=$(docker exec ${JENKINS_CONTAINER} cat /var/jenkins_home/secrets/initialAdminPassword 2>/dev/null)

# Se a senha não existir, define "admin"
if [ -z "$JENKINS_PASS" ]; then
    JENKINS_PASS="admin"
fi

# Pega o token anti-CSRF e salva a sessão no cookie
CRUMB=$(curl -s -c cookies.txt -u "${JENKINS_USER}:${JENKINS_PASS}" "http://${JENKINS_URL}/crumbIssuer/api/xml?xpath=concat(//crumbRequestField,\":\",//crumb)")

# Faz a requisição mandando a pipeline rodar
curl -s -X POST "http://${JENKINS_URL}/job/${JENKINS_JOB}/build" \
     -u "${JENKINS_USER}:${JENKINS_PASS}" \
     -b cookies.txt \
     -H "${CRUMB}"

echo -e "\nComando de build enviado com sucesso"

# Limpa o arquivo temporário de cookies
rm -f cookies.txt