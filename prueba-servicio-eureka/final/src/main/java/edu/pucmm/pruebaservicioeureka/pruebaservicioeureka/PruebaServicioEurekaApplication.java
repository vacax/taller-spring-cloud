package edu.pucmm.pruebaservicioeureka.pruebaservicioeureka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@EnableDiscoveryClient
@SpringBootApplication
public class PruebaServicioEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(PruebaServicioEurekaApplication.class, args);
    }

}

@RestController
class PruebaEurekaController{

    @Autowired
    DiscoveryClient discoveryClient;

    /**
     * http://localhost:8001/prueba-servicio-eureka
     * @param nombreAplicacion
     * @return
     */
    @RequestMapping("/{nombreAplicacion}")
    public List<ServiceInstance> listaInstanciaPorNombre(@PathVariable String nombreAplicacion){
        return discoveryClient.getInstances(nombreAplicacion);
    }
}
