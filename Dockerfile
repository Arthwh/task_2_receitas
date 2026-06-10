FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/registro-receitas.jar app.jar

# Define a porta que o container vai expor internamente
EXPOSE 80

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]