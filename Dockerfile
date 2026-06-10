FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/seu-projeto-0.0.1-SNAPSHOT.jar app.jar

# Etapa 2: Execução (Imagem leve)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Busca o .jar compilado
COPY target/seu-projeto-0.0.1-SNAPSHOT.jar app.jar

# Define a porta que o container vai expor internamente
EXPOSE 80

# Comando para rodar a aplicação
ENTRYPOINT ["nohup","java", "-jar", "app.jar"]