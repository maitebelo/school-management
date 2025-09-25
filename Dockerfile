# Use a imagem oficial do OpenJDK 17 como base
FROM openjdk:17-jdk-slim

# Define o diretório de trabalho dentro do container
WORKDIR /app

# Copia o arquivo pom.xml primeiro (para aproveitar o cache do Docker)
COPY pom.xml .

# Copia os wrappers do Maven
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

# Torna o mvnw executável
RUN chmod +x mvnw

# Baixa as dependências (isso será cacheado se o pom.xml não mudar)
RUN ./mvnw dependency:go-offline -B

# Copia o código fonte
COPY src src

# Compila a aplicação
RUN ./mvnw clean package -DskipTests

# Expõe a porta 8080
EXPOSE 8080

# Define o comando para executar a aplicação
CMD ["java", "-jar", "target/school-management-0.0.1-SNAPSHOT.jar"]
