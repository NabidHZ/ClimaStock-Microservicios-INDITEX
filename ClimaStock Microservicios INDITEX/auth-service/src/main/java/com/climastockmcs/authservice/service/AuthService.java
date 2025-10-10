// src/main/java/com/climastockmcs/authservice/service/AuthService.java
package com.climastockmcs.authservice.service;

import com.climastockmcs.authservice.model.Role;
import com.climastockmcs.authservice.model.User;
import com.climastockmcs.authservice.repository.RolRepository;
import com.climastockmcs.authservice.repository.UserRepository;
import com.climastockmcs.authservice.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.regex.Pattern;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Patrón regex para validar el formato del email
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");


    // Método para autenticar a un usuario y generar un token JWT
    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));//Si no encuentra al usuario → lanza la excepción RuntimeException.

        // Verifica si la contraseña proporcionada coincide con la almacenada en la base de datos
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Contraseña incorrecta");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // Si la autenticación es exitosa, genera y devuelve un token JWT
        if (authentication.isAuthenticated()) {
            return jwtUtil.generateToken((org.springframework.security.core.userdetails.User) authentication.getPrincipal());
        } else {
            throw new RuntimeException("Credenciales inválidas");
        }
    }

    // Método para registrar un nuevo usuario
    public User register(String username, String password, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }
        if (password == null || password.length() < 8) {
            throw new RuntimeException("La contraseña debe tener al menos 8 caracteres");
        }
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new RuntimeException("Email inválido");
        }
        Role userRole = rolRepository.findByName("ROLE_USER")
                .orElseGet(() -> rolRepository.save(new Role(null, "ROLE_USER")));
        Role adminRole = rolRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> rolRepository.save(new Role(null, "ROLE_ADMIN")));
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRoles(Collections.singleton(userRole));
        return userRepository.save(user);
    }
}
