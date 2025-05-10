package org.grupob.adminapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"org.grupob.comun","org.grupob.adminapp"})
@EnableJpaRepositories(basePackages = "org.grupob.comun.repository")
@EntityScan(basePackages = "org.grupob.comun.entity")
public class AdminAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminAppApplication.class, args);
    }

}
