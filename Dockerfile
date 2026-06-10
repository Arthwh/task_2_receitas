# Build
FROM maven:3.9-eclipse-temurin-21 AS construtor
WORKDIR /app
# [Aqui você copia o pom.xml, código fonte e roda o comando do maven]

# Etapa 2: Execução (Imagem leve)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# O segredo está aqui: você busca o jar lá da etapa anterior
COPY --from=construtor /app/target/seu-projeto-0.0.1-SNAPSHOT.jar app.jar

# Define a porta que o container vai expor internamente
EXPOSE 80

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]