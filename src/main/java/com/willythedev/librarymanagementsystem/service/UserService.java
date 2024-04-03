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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private JwtAuthenticationManager jwtAuthenticationManager;
  private PasswordEncoder passwordEncoder;
  private SystemUserRepository systemUserRepository;
  private TokenProvider tokenProvider;

  public UniversalResponse loginUser(LoginRequest loginRequest) {
    Authentication authentication =
        new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());
    Authentication authenticated = jwtAuthenticationManager.authenticate(authentication);

    String generatedToken = tokenProvider.generateToken(authenticated);
    return new UniversalResponse(200, "Login successful", Map.of("token", generatedToken));
  }

  public UniversalResponse registerAdmin(CreateSystemUserDto createSystemUserDto) {
    return createSystemUser(createSystemUserDto, "ROLE_ADMIN");
  }

  public UniversalResponse registerPatron(CreateSystemUserDto createSystemUserDto) {
    return createSystemUser(createSystemUserDto, "ROLE_PATRON");
  }

  @Lazy
  @Autowired
  public void setJwtAuthenticationManager(JwtAuthenticationManager jwtAuthenticationManager) {
    this.jwtAuthenticationManager = jwtAuthenticationManager;
  }

  @Lazy
  @Autowired
  public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Lazy
  @Autowired
  public void setSystemUserRepository(SystemUserRepository systemUserRepository) {
    this.systemUserRepository = systemUserRepository;
  }

  @Lazy
  @Autowired
  public void setTokenProvider(TokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
  }

  private UniversalResponse createSystemUser(CreateSystemUserDto createSystemUserDto, String role) {
    Optional<SystemUser> existingSystemUser =
        systemUserRepository.findByEmail(createSystemUserDto.email());

    if (existingSystemUser.isPresent()) {
      throw new ItemExistsException("User with email exists");
    }

    SystemUser systemUser =
        SystemUser.builder()
            .email(createSystemUserDto.email())
            .password(passwordEncoder.encode(createSystemUserDto.password()))
            .role(role)
            .build();
    systemUserRepository.save(systemUser);

    return new UniversalResponse(200, "User registered successfully", Map.of());
  }
}
