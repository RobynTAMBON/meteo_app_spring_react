# Étape 1 : builder le projet
FROM maven:3.9.2-eclipse-temurin-17 AS build
WORKDIR /app

# Copier les fichiers du projet
COPY pom.xml mvnw ./
COPY .mvn .mvn
COPY src src

# Rendre mvnw exécutable
RUN chmod +x mvnw

# Build le back-end (Spring Boot)
RUN ./mvnw clean package -DskipTests

# Étape 2 : préparer l'image finale
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copier le JAR généré depuis l'étape build
COPY --from=build /app/target/*.jar app.jar

# Exposer le port de l'application Spring Boot
EXPOSE 8080

# Commande pour lancer Spring Boot
ENTRYPOINT ["java","-jar","app.jar"]
