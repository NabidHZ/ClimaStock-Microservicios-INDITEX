package com.climastockmcs.authservice.service;

import com.climastockmcs.authservice.model.User;
import com.climastockmcs.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;


//La annotacion @service significa que Spring la detecta automáticamente y la puede inyectar en otros componentes.
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    //UserDetailsService es una interfaz que Spring Security usa internamente cuando alguien intenta hacer login.

    //Inyección de dependencia del repositorio de usuarios
    @Autowired
    private UserRepository userRepository;

    //Este método carga los detalles del usuario por su nombre de usuario
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
                //Si lo encuentra → lo guarda en la variable user.
                //Si no encuentra al usuario → lanza la excepción UsernameNotFoundException.



        //GrantedAuthority representa un permiso o rol asignado a un usuario.
        Set<GrantedAuthority> authorities = user.getRoles()
                .stream() //Convierte la colección de roles en un flujo
                .map(role -> new SimpleGrantedAuthority(role.getName())) //SimplGrantedAuthority Guarda simplemente el nombre del permiso o rol..
                .collect(Collectors.toSet());// Convierte el flujo de objetos GrantedAuthority en un conjunto (Set).
        //


        //Finalmente, crea y devuelve un objeto UserDetails utilizando la clase User de Spring Security,
        // que implementa la interfaz UserDetails.
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
