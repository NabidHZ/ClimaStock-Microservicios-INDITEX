package com.climastockmcs.authservice.controller;

import com.climastockmcs.authservice.dto.AuthRequest;
import com.climastockmcs.authservice.dto.AuthResponse;
import com.climastockmcs.authservice.model.User;
import com.climastockmcs.authservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody AuthRequest request) {
        User user = authService.register(request.getUsername(), request.getPassword(), request.getEmail());
        return ResponseEntity.ok(user);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        String token = authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
