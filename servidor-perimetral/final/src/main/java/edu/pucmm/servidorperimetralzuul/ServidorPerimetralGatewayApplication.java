package edu.pucmm.servidorperimetralzuul;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;



@EnableDiscoveryClient
@SpringBootApplication
public class ServidorPerimetralGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServidorPerimetralGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        //Configurando las rutas programatica.
        return builder.routes()

                /**
                 * Mediante el Gateway, el trafico enviado por la URL /micro1 (definido en el path), se estará enviando
                 * al servicio microservicio-estudiante, el filtro elimina el path de micro1 al servicio
                 * para que la consulta funcione, es decir, cuando llamo por le metodo get
                 * http://localhost:8081/micro1/estudiante/ el gateway sustituye por http://localhost:xxxx/, las xxxx
                 * corresponde al puerto random donde esté corriendo el servidor. Esa configuracion es realziada con
                 * el filtro rewritepath.
                 */
                .route("micro1",p -> p.path("/micro1/**")
                        .filters(f -> f.rewritePath("/micro1/(?<segment>.*)", "/${segment}"))
                        .uri("lb://microservicio-estudiante"))

                /**
                 * En la configuracion mostrada, mediante la ruta /imagenes/, estaremos enviando la peticion directamente
                 * al endpoint /image/**, ubicado en la URI: https://httpbin.org/.
                 */
                .route("imagenes", p -> p.path("/imagenes/**")
                        .filters(f -> f.rewritePath("/imagenes/(?<segment>.*)", "/image/${segment}"))
                        .uri("https://httpbin.org/"))
                .build();
    }

}
