// src/main/java/com/climastockmcs/authservice/config/DataInitializer.java
package com.climastockmcs.authservice.config;

import com.climastockmcs.authservice.model.Role;
import com.climastockmcs.authservice.model.User;
import com.climastockmcs.authservice.repository.RolRepository;
import com.climastockmcs.authservice.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {
    @Bean
    public CommandLineRunner initRoles(RolRepository rolRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) { //ComandLineRunner es una interfaz funcional que se utiliza para ejecutar código al iniciar la aplicación.
        return args -> {
            if (rolRepository.findByName("ROLE_USER").isEmpty()) { //Si no existe el rol "ROLE_USER", lo crea y lo guarda en la base de datos.
                rolRepository.save(new Role(null, "ROLE_USER"));
            }
            if (rolRepository.findByName("ROLE_ADMIN").isEmpty()) { //Si no existe el rol "ROLE_ADMIN", lo crea y lo guarda en la base de datos.
                rolRepository.save(new Role(null, "ROLE_ADMIN"));
            }
            // Crear usuario admin inicial si no existe
            if (userRepository.findByUsername("admin").isEmpty()) {
                Role adminRole = rolRepository.findByName("ROLE_ADMIN").orElseThrow();
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRoles(Set.of(adminRole));
                admin.setEmail("admin@climastock.com");
                userRepository.save(admin);
            }
        };
    }
}
