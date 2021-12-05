package edu.pucmm.monitorioapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

/**
 * Para acceder ir a http://localhost:4444/hystrix
 * Para le flujo de datos indicar la siguiente direccion:
 * http://localhost:4444/turbine.stream?cluster=MICROSERVICIO-ESTUDIANTE
 */
@EnableDiscoveryClient
@EnableHystrixDashboard
@EnableTurbine
@SpringBootApplication
public class MonitorioAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonitorioAppApplication.class, args);
    }

}
