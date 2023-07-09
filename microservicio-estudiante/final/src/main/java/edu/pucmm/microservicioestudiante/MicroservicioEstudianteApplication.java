package edu.pucmm.microservicioestudiante;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.helpers.BasicMarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;


import java.io.Serializable;
import java.util.List;
import java.util.Random;
import org.h2.tools.Server;
import java.sql.SQLException;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroservicioEstudianteApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroservicioEstudianteApplication.class, args);
    }

    /**
     * Para subir H2 modo servidor en Spring Boot.
     * @return
     * @throws SQLException
     */
    /*@Bean(initMethod = "start", destroyMethod = "stop")
    public Server inMemoryH2DatabaseaServer() throws SQLException {
        System.out.println("Iniciando Base de datos.");
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092", "-ifNotExists", "-tcpDaemon");
    }*/
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

    private static final Logger LOGGER = LoggerFactory.getLogger(AppController.class);

    @RequestMapping("/")
    public String app(HttpServletRequest request){
        LOGGER.info("Consultado la barra");
        return "Micro Servicio Estudiante por el puerto:"+request.getLocalPort();
    }

    /**
     * Simulando una parada del metodo por tiempo...
     * @return
     * @throws InterruptedException
     */
    //@HystrixCommand(fallbackMethod = "salidaCircuitoAbierto" )
    @RequestMapping("/simular-parada")
    public String simularParada()  {
        LOGGER.info("Prueba simulación de parada.");
        Random random = new Random();
        int valorGenerado = random.nextInt(3000);
        LOGGER.info("El valor generado: "+valorGenerado);
        if(valorGenerado > 1000){
            throw new RuntimeException("Error provocado...");
        }
        return "Mostrando información";
    }

    public String salidaCircuitoAbierto(){
        LOGGER.info("Circuito Abierto...");
        return "Con la ejecución del metodo.... Abriendo el circuito...";
    }

}

@RestController
@RequestMapping("/estudiante/")
class EstudianteController{

    private static final Logger LOGGER = LoggerFactory.getLogger(EstudianteController.class);

    @Autowired
    EstudianteRepository estudianteRepository;

    /**
     *
     * @return
     */
    @GetMapping("/")
    public List<Estudiante> getListaEstudiante(){
        Marker marker = new BasicMarkerFactory().getMarker("cantidad_estudiante");
        List<Estudiante> all = estudianteRepository.findAll();
        LOGGER.info(marker, ""+all.size());
        return all;
    }

    @PostMapping(value = "/")
    public Estudiante crearEstudiante(@RequestBody Estudiante estudiante){
        LOGGER.info("Recibido el objeto: "+estudiante);
        estudianteRepository.save(estudiante);
        LOGGER.info("Guardando Estudiante: "+estudiante);
        return estudiante;
    }

    //omitiendo los demás servicios en el ejemplo
}

