package edu.pucmm.pruebaserviciocentralizacionconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
public class PruebaServicioCentralizacionConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(PruebaServicioCentralizacionConfigApplication.class, args);
    }

}

@RefreshScope
@RestController
class PruebaRestController{

    @Value("${barcamp}")
    private String barcamp;

    @RequestMapping("/")
    public String holaMundo(){
        return barcamp;
    }
}
