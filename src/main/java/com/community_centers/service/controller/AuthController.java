package com.community_centers.service.controller;

import com.community_centers.service.dto.LoginRequest;
import com.community_centers.service.dto.JwtResponse;
import com.community_centers.service.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        logger.info("Login attempt for user: {}", loginRequest.getUsername());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            logger.info("Authentication successful for user: {}", loginRequest.getUsername());

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);

            logger.info("JWT token generated successfully for user: {}", loginRequest.getUsername());

            return ResponseEntity.ok(new JwtResponse(jwt));

        } catch (BadCredentialsException e) {
            logger.warn("Bad credentials for user: {}", loginRequest.getUsername());
            return ResponseEntity.status(401).body(
                    Map.of("error", "Invalid credentials", "message", "Wrong username or password")
            );
        } catch (Exception e) {
            logger.error("Login error for user {}: {}", loginRequest.getUsername(), e.getMessage(), e);
            return ResponseEntity.status(500).body(
                    Map.of("error", "Login failed", "message", e.getMessage())
            );
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of("status", "OK", "service", "auth"));
    }
}