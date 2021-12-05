# Probando el concepto de Multi-stage.
# Instalando Gradle para compilar al aplicación y luego lo necesario a una imagen completa.
FROM gradle:5.6.4-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootJar --no-daemon

# Utilizando uan imagen con lo necesario para arrancar la aplicación.
# en Java las imagenes slim son las mas pequeñas en ese tipo.
FROM openjdk:11.0.12-jre-slim-buster
# Indicando el puerto para exponer, debo pasar el flag -p para habilitarlo o -P para publicarlos todos.
# No es necesario exponer ningun puerto, la propia instancia se reportará a Eureka.
# EXPOSE 8080
# creando la carpeta para el proyecto
RUN mkdir /app
# desde la otra instancia estaremos copiando lo necesario
COPY --from=build /home/gradle/src/build/libs/*.jar /app/microservicio.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/microservicio.jar"]
