package com.climastockmcs.authservice.security;


import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component //Esta clase es un componente que quiero que esté disponible para inyección (@Autowired)
public class JwtUtil {


    //Esto significa que los valores de secret y expiration se leerán del archivo application.yml
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }
    //userDetails.getUsername() → el nombre de usuario (por ejemplo, “juan”)
    //claims → son datos adicionales (puedes añadir lo que quieras, como roles, email, etc.).



    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims) //Añade información adicional dentro del token.
                .setSubject(subject) //Establece el “dueño” del token, "username" en mi caso
                .setIssuedAt(new Date(System.currentTimeMillis())) //Fecha y hora en que se crea el token.
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) //Cuándo expira (ahora + tiempo configurado).
                .signWith(SignatureAlgorithm.HS256, secret.getBytes()) //Firma el token con el algoritmo HS256 y la clave secreta.
                .compact(); //Lo convierte en el string JWT final.
    }


    //Esto lee el token y devuelve el username que estaba guardado dentro (subject).
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    //Esto lee el token y devuelve la fecha de expiración.
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    //Este método es genérico y puede extraer cualquier dato del token.
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        if (token == null || token.trim().isEmpty()) {
            return null;
        }
        final Claims claims = extractAllClaims(token);
        return claims != null ? claimsResolver.apply(claims) : null;
    }

    //Este método extrae todos los datos (claims) del token.
    private Claims extractAllClaims(String token) {
        if (token == null || token.trim().isEmpty()) {
            return null;
        }
        return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
    }

    //Este método comprueba si el token ha expirado.
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //Este método valida el token comparando el username del token con el del UserDetails
    //y comprobando que no haya expirado.
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
