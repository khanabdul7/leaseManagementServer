package com.example.lease_management.Controller;

import com.example.lease_management.JWT.JwtUtil;
import com.example.lease_management.dto.Login.LoginDTO;
import com.example.lease_management.dto.Login.LoginRequest;
import com.example.lease_management.dto.Register.RegisterDTO;
import com.example.lease_management.dto.Register.RegisterRequest;
import com.example.lease_management.service.RegisterService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class RegisterController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RegisterService registerService;

    public RegisterController(AuthenticationManager authenticationManager,
                              JwtUtil jwtUtil, RegisterService registerService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.registerService = registerService;
    }

    @PostMapping("/register")
    public RegisterDTO register(@RequestBody RegisterRequest request) {

        registerService.register(request);

        return new RegisterDTO("User registered successfully");
    }

    @PostMapping("/login")
    public LoginDTO login(@RequestBody LoginRequest request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(), request.password()
                )
        );

        String token = jwtUtil.generateToken(request.username());
        return new LoginDTO(token, request.username());
    }
}
