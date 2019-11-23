package edu.pucmm.microservicioestudiante;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Random;

@EnableDiscoveryClient
@EnableCircuitBreaker
@SpringBootApplication
public class MicroservicioEstudianteApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroservicioEstudianteApplication.class, args);
    }

}

/**
 * Entidad del Estudiante
 */
@Entity
class Estudiante implements Serializable{

    @Id
    String matricula;
    String nombre;
    String carrera;

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    @Override
    public String toString() {
        return "Estudiante{" +
                "matricula=" + matricula +
                ", nombre='" + nombre + '\'' +
                ", carrera='" + carrera + '\'' +
                '}';
    }
}

/**
 * Configurando la funcionalidad del Data Rest
 */
//@RepositoryRestResource(collectionResourceRel = "estudiantes", path = "estudiante")
@Repository
interface EstudianteRepository extends JpaRepository<Estudiante, Integer>{

}

@RestController
@RequestMapping("/")
class AppController{

    @RequestMapping("/")
    public String app(HttpServletRequest request){
        return "Micro Servicio Estudiante por el puerto:"+request.getLocalPort();
    }

    /**
     * Simulando una parada del metodo por tiempo...
     * @return
     * @throws InterruptedException
     */
    @HystrixCommand(fallbackMethod = "salidaCircuitoAbierto", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
    })
    @RequestMapping("/simular-parada")
    public String simularParada() throws InterruptedException {
        Random random = new Random();
        int valorGenerado = random.nextInt(3000);
        System.out.println("El valor generado: "+valorGenerado);
        Thread.sleep(valorGenerado);
        return "Dato que no debe presentarse...";
    }

    public String salidaCircuitoAbierto(){
        return "Problema con el tiempo de ejecucion...";
    }

}

@RestController
@RequestMapping("/estudiante/")
class EstudianteController{

    @Autowired
    EstudianteRepository estudianteRepository;

    /**
     *
     * @return
     */
    @GetMapping("/")
    public List<Estudiante> getListaEstudiante(){
        return estudianteRepository.findAll();
    }

    @PostMapping(value = "/")
    public Estudiante crearEstudiante(@RequestBody Estudiante estudiante){
        System.out.println("recibido el objeto: "+estudiante);
        estudianteRepository.save(estudiante);
        return estudiante;
    }

    //omitiendo los demás servicios en el ejemplo
}

