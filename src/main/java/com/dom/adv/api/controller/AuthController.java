package com.dom.adv.api.controller;

import com.dom.adv.api.dto.AuthResponse;
import com.dom.adv.api.dto.LoginRequest;
import com.dom.adv.api.dto.RegisterRequest;
import com.dom.adv.api.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try {
            // Registrar al usuario y generar el JWT
            String token = authService.register(request);
            return ResponseEntity.ok("Usuario registrado con éxito. Token: " + token);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al registrar el usuario: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            // Autenticar al usuario y generar el JWT
            String token = authService.authenticate(request);
            AuthResponse response = new AuthResponse(token);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new AuthResponse("Credenciales incorrectas"));
        }
    }
}
