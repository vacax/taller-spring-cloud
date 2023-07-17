# Probando el concepto de Multi-stage.
# Instalando Gradle para compilar al aplicación y luego lo necesario a una imagen completa.
FROM gradle:8.1.1-jdk17-jammy AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootJar --no-daemon

# Utilizando uan imagen con lo necesario para arrancar la aplicación.
# en Java las imagenes slim son las mas pequeñas en ese tipo.
FROM eclipse-temurin:17.0.7_7-jre-alpine
# Indicando el puerto para exponer, debo pasar el flag -p para habilitarlo o -P para publicarlos todos.
EXPOSE 8181
# creando la carpeta para el proyecto
RUN mkdir /app
# desde la otra instancia estaremos copiando lo necesario
COPY --from=build /home/gradle/src/build/libs/*.jar /app/cliente-app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/cliente-app.jar"]
