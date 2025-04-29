package org.grupob.empapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.grupob.comun","org.grupob.empapp"})
public class EmpAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmpAppApplication.class, args);
	}

}
