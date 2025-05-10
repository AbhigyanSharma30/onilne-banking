package com.hindfundsbank.controller;

import com.hindfundsbank.dto.*;
import com.hindfundsbank.model.User;
import com.hindfundsbank.repository.UserRepository;
import com.hindfundsbank.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
  @Autowired private AuthenticationManager authManager;
  @Autowired private UserRepository userRepo;
  @Autowired private PasswordEncoder passwordEncoder;
  @Autowired private JwtUtil jwtUtil;

  @PostMapping("/signup")
  public ResponseEntity<String> register(@RequestBody RegisterRequest req) {
    if (userRepo.existsByEmail(req.email)) {
      return ResponseEntity.badRequest().body("Email already in use");
    }
    User u = new User();
    u.setName(req.name);
    u.setEmail(req.email);
    u.setPassword(passwordEncoder.encode(req.password));
    userRepo.save(u);
    return ResponseEntity.status(HttpStatus.CREATED).body("User registered");
  }

  @PostMapping("/signin")
  public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
    try {
      authManager.authenticate(
        new UsernamePasswordAuthenticationToken(req.email, req.password));
      String token = jwtUtil.generateToken(req.email);
      return ResponseEntity.ok(new AuthResponse(token));
    } catch (BadCredentialsException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }
}
