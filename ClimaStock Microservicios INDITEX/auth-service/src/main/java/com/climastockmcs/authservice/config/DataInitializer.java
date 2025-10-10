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
    public CommandLineRunner initRoles(RolRepository rolRepository) {
        return args -> {
            if (rolRepository.findByName("ROLE_USER").isEmpty()) {
                rolRepository.save(new Role(null, "ROLE_USER"));
            }
            if (rolRepository.findByName("ROLE_ADMIN").isEmpty()) {
                rolRepository.save(new Role(null, "ROLE_ADMIN"));
            }
        };
    }
}
