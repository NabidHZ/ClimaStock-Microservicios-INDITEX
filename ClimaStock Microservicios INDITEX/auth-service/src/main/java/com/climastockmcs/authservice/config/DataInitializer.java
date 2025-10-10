// src/main/java/com/climastockmcs/authservice/config/DataInitializer.java
package com.climastockmcs.authservice.config;

import com.climastockmcs.authservice.model.Role;
import com.climastockmcs.authservice.repository.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner initRoles(RolRepository rolRepository) { //ComandLineRunner es una interfaz funcional que se utiliza para ejecutar código al iniciar la aplicación.
        return args -> {
            if (rolRepository.findByName("ROLE_USER").isEmpty()) { //Si no existe el rol "ROLE_USER", lo crea y lo guarda en la base de datos.
                rolRepository.save(new Role(null, "ROLE_USER"));
            }
            if (rolRepository.findByName("ROLE_ADMIN").isEmpty()) { //Si no existe el rol "ROLE_ADMIN", lo crea y lo guarda en la base de datos.
                rolRepository.save(new Role(null, "ROLE_ADMIN"));
            }
        };
    }
}
