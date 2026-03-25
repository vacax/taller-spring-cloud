package edu.pucmm.servidorperimetralzuul;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions;
import org.springframework.cloud.gateway.server.mvc.filter.LoadBalancerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.RequestPredicates.path;


@EnableDiscoveryClient
@SpringBootApplication
public class ServidorPerimetralGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServidorPerimetralGatewayApplication.class, args);
    }

    /**
     * Configuración global de CORS para el Gateway.
     * Permite que el navegador realice llamadas a la API desde cualquier origen,
     * evitando el bloqueo por política de Same-Origin.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true)
                        .maxAge(3600);
            }
        };
    }

    /**
     * Mediante el Gateway, el trafico enviado por la URL /micro1 (definido en el path), se estará enviando
     * al servicio microservicio-estudiante, el filtro elimina el path de micro1 al servicio
     * para que la consulta funcione, es decir, cuando llamo por el metodo get
     * http://localhost:8081/micro1/estudiante/ el gateway sustituye por http://localhost:xxxx/, las xxxx
     * corresponde al puerto random donde esté corriendo el servidor. Esa configuracion es realizada con
     * el filtro rewritePath.
     *
     * NOTA: En Spring Cloud Gateway MVC (no reactivo), se utiliza RouterFunction<ServerResponse>
     * en lugar de RouteLocator/RouteLocatorBuilder que era la API del Gateway reactivo (WebFlux).
     * El URI destino se define con FilterFunctions.uri() y el handler HandlerFunctions.http()
     * no recibe argumentos — lee el URI desde los atributos del request.
     */
    @Bean
    public RouterFunction<ServerResponse> micro1Route() {
        return GatewayRouterFunctions.route("micro1")
                .route(path("/micro1/**"), HandlerFunctions.http())
                .filter(LoadBalancerFilterFunctions.lb("microservicio-estudiante"))
                .filter(FilterFunctions.rewritePath("/micro1/(?<segment>.*)", "/${segment}"))
                .build();
    }

    /**
     * En la configuracion mostrada, mediante la ruta /imagenes/, estaremos enviando la peticion directamente
     * al endpoint /image/**, ubicado en la URI: https://httpbin.org/.
     */
    @Bean
    public RouterFunction<ServerResponse> imagenesRoute() {
        return GatewayRouterFunctions.route("imagenes")
                .route(path("/imagenes/**"), HandlerFunctions.http())
                .filter(FilterFunctions.uri("https://httpbin.org/"))
                .filter(FilterFunctions.rewritePath("/imagenes/(?<segment>.*)", "/image/${segment}"))
                .build();
    }

}
