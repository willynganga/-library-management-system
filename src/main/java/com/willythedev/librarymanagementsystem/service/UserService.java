package com.willythedev.librarymanagementsystem.service;

import com.willythedev.librarymanagementsystem.exception.ItemExistsException;
import com.willythedev.librarymanagementsystem.model.SystemUser;
import com.willythedev.librarymanagementsystem.repository.SystemUserRepository;
import com.willythedev.librarymanagementsystem.security.JwtAuthenticationManager;
import com.willythedev.librarymanagementsystem.security.TokenProvider;
import com.willythedev.librarymanagementsystem.wrapper.CreateSystemUserDto;
import com.willythedev.librarymanagementsystem.wrapper.LoginRequest;
import com.willythedev.librarymanagementsystem.wrapper.UniversalResponse;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final JwtAuthenticationManager jwtAuthenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final SystemUserRepository systemUserRepository;
  private final TokenProvider tokenProvider;

  public UniversalResponse loginUser(LoginRequest loginRequest) {
    Authentication authentication =
        new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());
    Authentication authenticated = jwtAuthenticationManager.authenticate(authentication);

    String generatedToken = tokenProvider.generateToken(authenticated);
    return new UniversalResponse(200, "Login successful", Map.of("token", generatedToken));
  }

  public UniversalResponse registerAdmin(CreateSystemUserDto createSystemUserDto) {
    Optional<SystemUser> existingSystemUser =
        systemUserRepository.findByEmail(createSystemUserDto.email());

    if (existingSystemUser.isPresent()) {
      throw new ItemExistsException("User with email exists");
    }

    SystemUser systemUser =
        SystemUser.builder()
            .email(createSystemUserDto.email())
            .password(passwordEncoder.encode(createSystemUserDto.password()))
            .role("ROLE_ADMIN")
            .build();
    systemUserRepository.save(systemUser);

    return new UniversalResponse(200, "User registered successfully", Map.of());
  }
}
