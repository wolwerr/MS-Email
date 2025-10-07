# Primeira etapa: Construir a aplicação
FROM maven:3.9.5-amazoncorretto-17 AS build

WORKDIR /workspace

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src src
ARG MAVEN_SKIP_TEST=false
RUN if [ "$MAVEN_SKIP_TEST" = "true" ] ; then mvn clean package -DskipTests ; else mvn clean package ; fi

# Segunda etapa: Rodar a aplicação
FROM amazoncorretto:17

LABEL maintainer="ricardo@ricardo.net"
LABEL version="1.0"
LABEL description="Backend Portifólio E-mail"
LABEL name="MS-Email"

EXPOSE 8080

COPY --from=build /workspace/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
