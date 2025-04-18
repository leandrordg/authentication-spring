package com.lbertalhia.security.controllers;

import com.lbertalhia.security.dtos.LoginRequestDto;
import com.lbertalhia.security.dtos.LoginResponseDto;
import com.lbertalhia.security.dtos.RegisterUserDto;
import com.lbertalhia.security.entities.User;
import com.lbertalhia.security.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto dto) {
        return authService.register(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto) {
        return authService.login(dto);
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return authService.getAllUsers();
    }
}
