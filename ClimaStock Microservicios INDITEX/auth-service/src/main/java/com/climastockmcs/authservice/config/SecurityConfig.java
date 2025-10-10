package com.climastockmcs.authservice.config;

import com.climastockmcs.authservice.security.JwtFilter;
import com.climastockmcs.authservice.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration //Indica que esta clase contiene configuraciones de Spring
@EnableWebSecurity //Habilita la seguridad web en la aplicación
public class SecurityConfig {

    @Autowired //Inyección de dependencia del filtro JWT
    private JwtFilter jwtFilter;

    @Autowired //se usa para obtener la información del usuario (como nombre y roles) cuando se valida el token.
    private UserDetailsServiceImpl userDetailsService;


    //Bean que define el codificador de contraseñas utilizando BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //BCrypt es un algoritmo de hashing seguro para almacenar contraseñas.
    }

    //Bean AuthenticationManager es quien comprueba usuario y contraseña cuando alguien hace login.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    //Bean que configura la cadena de filtros de seguridad
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) //Desactiva la protección CSRF porque ya uso JWT
                .authorizeHttpRequests(auth -> auth //Define qué rutas necesitan autenticación y cuáles no.
                        .requestMatchers("/auth/register", "/auth/login", "/login", "/register").permitAll() // 🔹 Endpoints públicos y páginas HTML
                        .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("USER", "ADMIN") // Solo lectura para USER y ADMIN
                        .requestMatchers("/api/**").hasRole("ADMIN") // Crear, editar, eliminar solo ADMIN
                        .anyRequest().authenticated() //Cualquier otra ruta requiere autenticación
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                //Significa que no se guardará sesión en el servidor.
               // En una app JWT, cada petición lleva su token,
               //por eso no hace falta que el servidor recuerde quién eres (no hay “sesión”).


        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); //Añade el filtro JWT antes del filtro de autenticación por defecto de Spring Security.

        return http.build(); //Construye y devuelve la configuración de seguridad.
    }
}
