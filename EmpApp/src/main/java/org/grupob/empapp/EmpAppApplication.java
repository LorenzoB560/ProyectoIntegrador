package org.grupob.empapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"org.grupob.comun","org.grupob.empapp"})
@EnableJpaRepositories(basePackages = "org.grupob.comun.repository")
@EntityScan(basePackages = "org.grupob.comun.entity")
public class EmpAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmpAppApplication.class, args);
	}

}
