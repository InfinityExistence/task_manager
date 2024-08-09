FROM maven as builder
WORKDIR /build
COPY mvnw pom.xml ./
RUN mvn verify --fail-never
COPY ./src ./src
RUN mvn clean install -DskipTests

FROM eclipse-temurin:21-jdk-alpine
RUN addgroup --system task-manager-group && adduser --system --ingroup task-manager-group task-manager
USER task-manager:task-manager-group
WORKDIR task-manager
EXPOSE 3005
COPY --from=builder /build/target/*.jar /task-manager/app.jar
ENTRYPOINT ["java","-jar","/task-manager/app.jar"]