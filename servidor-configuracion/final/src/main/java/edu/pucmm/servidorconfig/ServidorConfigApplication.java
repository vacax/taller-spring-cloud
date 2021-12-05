package edu.pucmm.servidorconfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableConfigServer
@EnableDiscoveryClient
@SpringBootApplication
public class ServidorConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServidorConfigApplication.class, args);
    }

}
