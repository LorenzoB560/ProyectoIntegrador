package org.grupob.adminapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@ComponentScan(basePackages = {"org.grupob.comun","org.grupob.adminapp"})
public class AdminAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminAppApplication.class, args);
    }

}
