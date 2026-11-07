
# Stage 1: Build using Amazon Corretto 25
FROM amazoncorretto:25 AS build

WORKDIR /app

# Install Maven
RUN dnf install -y maven && dnf clean all

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests


# Stage 2: Run using Amazon Corretto 25
FROM amazoncorretto:25

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
