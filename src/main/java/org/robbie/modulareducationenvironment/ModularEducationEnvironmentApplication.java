package org.robbie.modulareducationenvironment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ModularEducationEnvironmentApplication {
    public static final String environmentPath = "org.robbie.modulareducationenvironment";
    public static void main(String[] args) {
        SpringApplication.run(ModularEducationEnvironmentApplication.class, args);
    }

}
