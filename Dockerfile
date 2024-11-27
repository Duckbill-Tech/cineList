# ----------------------------
# Etapa 1: Construir o Frontend
# ----------------------------
FROM node:18-alpine AS frontend-builder

WORKDIR /app

# Copia os arquivos do frontend
COPY spa/package*.json ./
COPY spa/ ./

# Instala as dependências e constrói o frontend
RUN npm install
RUN npm run build

# ----------------------------
# Etapa 2: Construir o Backend
# ----------------------------
FROM maven:3.8.7-eclipse-temurin-17-alpine AS backend-builder

WORKDIR /app

# Copia os arquivos do backend
COPY pom.xml .
COPY src ./src

# Copia o build do frontend para o diretório de recursos estáticos do backend
COPY --from=frontend-builder /app/dist ./src/main/resources/static

# Compila o backend
RUN mvn clean package -DskipTests

# ----------------------------
# Etapa 3: Imagem Final
# ----------------------------
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copia o JAR gerado para a imagem final
COPY --from=backend-builder /app/target/*.jar app.jar

# Porta que a aplicação irá expor
EXPOSE 8081

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
