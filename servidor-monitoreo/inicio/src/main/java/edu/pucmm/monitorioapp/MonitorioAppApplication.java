package edu.pucmm.monitorioapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

/**
 * Para acceder ir a http://localhost:4444/hystrix
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
