# Monitoreo sobre ZIPKIN

Desde la versión 3 de Spring Boot, se utiliza Micrometer
para el seguimiento. Es necesario levantar el servico vía en Docker:

```
docker run -d -p 9411:9411 openzipkin/zipkin
```

Ver documentación oficial en https://zipkin.io/pages/quickstart

Una vez levantado el servicio puedes acceder en http://127.0.0.1:9411/
