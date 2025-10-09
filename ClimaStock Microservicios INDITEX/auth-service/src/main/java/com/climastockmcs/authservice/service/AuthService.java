package com.climastockmcs.authservice.service;

import com.climastockmcs.authservice.model.Role;
import com.climastockmcs.authservice.model.User;
import com.climastockmcs.authservice.repository.RolRepository;
import com.climastockmcs.authservice.repository.UserRepository;
import com.climastockmcs.authservice.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;


//Es el servicio que maneja el login y el registro de usuarios.
//Mientras que las otras clases eran configuraciones (como SecurityConfig, JwtFilter, JwtUtil...),
//esta es la que pone esas piezas a trabajar juntas.
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

    //Metdodo login
    public String login(String username, String password) { //Recibe el nombre de usuario y la contraseña.
        Authentication authentication = authenticationManager.authenticate( //Llama al AuthenticationManager para autenticar las credenciales.
                new UsernamePasswordAuthenticationToken(username, password) //Crea un token de autenticación con las credenciales proporcionadas.
        );

        if (authentication.isAuthenticated()) {
            return jwtUtil.generateToken((org.springframework.security.core.userdetails.User) authentication.getPrincipal());
            //Si todo va bien → genera el JWT con jwtUtil.generateToken().
        } else {
            throw new RuntimeException("Credenciales inválidas");
            //Si no va bien → lanza un errorr
        }
    }

    //Método register
    public User register(String username, String password, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }
        Role userRole = rolRepository.findByName("ROLE_USER")
                .orElseGet(() -> rolRepository.save(new Role(null, "ROLE_USER")));
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRoles(Collections.singleton(userRole));
        return userRepository.save(user);
    }

}
