# Step 1: Build the Spring Boot JAR with Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Step 2: Run with lightweight JRE 17 image
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
RUN mkdir -p /app/data
COPY --from=build /app/target/*.jar app.jar

ENV PORT=8080
ENV SPRING_DATASOURCE_URL="jdbc:h2:file:/app/data/freelancehub;MODE=MySQL;DATABASE_TO_LOWER=TRUE;AUTO_SERVER=TRUE"
ENV SPRING_DATASOURCE_USERNAME="sa"
ENV SPRING_DATASOURCE_PASSWORD=""
EXPOSE 8080

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
