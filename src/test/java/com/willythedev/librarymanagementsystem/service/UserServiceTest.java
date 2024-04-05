package com.willythedev.librarymanagementsystem.service;

import static org.junit.jupiter.api.Assertions.*;

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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
  @Mock private JwtAuthenticationManager jwtAuthenticationManager;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private SystemUserRepository systemUserRepository;
  @Mock private TokenProvider tokenProvider;
  private UserService userService;

  @Test
  void canLoginUserTest() {
    LoginRequest loginRequest = new LoginRequest("test@test.com", "test123456");
    Authentication authenticationToken =
        new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());

    Mockito.when(jwtAuthenticationManager.authenticate(authenticationToken))
        .thenReturn(authenticationToken);
    Mockito.when(tokenProvider.generateToken(authenticationToken)).thenReturn("sample-token");

    UniversalResponse universalResponse = userService.loginUser(loginRequest);
    ArgumentCaptor<Authentication> authenticationArgumentCaptor =
        ArgumentCaptor.forClass(Authentication.class);
    Mockito.verify(jwtAuthenticationManager).authenticate(authenticationArgumentCaptor.capture());

    Assertions.assertEquals(
        loginRequest.email(), authenticationArgumentCaptor.getValue().getName());
    Assertions.assertEquals(
        loginRequest.password(), authenticationArgumentCaptor.getValue().getCredentials());
    Assertions.assertEquals(200, universalResponse.status());
    Assertions.assertEquals("Login successful", universalResponse.message());
    Assertions.assertEquals(Map.of("token", "sample-token"), universalResponse.data());
    verifyNoMoreInteractions();
  }

  @Test
  void canRegisterAdminTest() {
    CreateSystemUserDto createSystemUserDto =
        new CreateSystemUserDto("test@test.com", "test123456");

    Mockito.when(systemUserRepository.findByEmail(createSystemUserDto.email()))
        .thenReturn(Optional.empty());
    Mockito.when(passwordEncoder.encode(createSystemUserDto.password()))
        .thenReturn("encrypted-password");

    UniversalResponse universalResponse = userService.registerAdmin(createSystemUserDto);

    ArgumentCaptor<SystemUser> systemUserArgumentCaptor = ArgumentCaptor.forClass(SystemUser.class);
    Mockito.verify(systemUserRepository).save(systemUserArgumentCaptor.capture());

    SystemUser systemUser = systemUserArgumentCaptor.getValue();
    Assertions.assertEquals(createSystemUserDto.email(), systemUser.getEmail());
    Assertions.assertEquals("encrypted-password", systemUser.getPassword());
    Assertions.assertEquals("ROLE_ADMIN", systemUser.getRole());
    Assertions.assertEquals(200, universalResponse.status());
    Assertions.assertEquals("User registered successfully", universalResponse.message());
    verifyNoMoreInteractions();
  }

  @Test
  void cannotRegisterAdminWithNonUniqueEmailTest() {
    CreateSystemUserDto createSystemUserDto =
        new CreateSystemUserDto("test@test.com", "test123456");

    Mockito.when(systemUserRepository.findByEmail(createSystemUserDto.email()))
        .thenReturn(Optional.of(new SystemUser()));

    ItemExistsException itemExistsException =
        assertThrows(
            ItemExistsException.class, () -> userService.registerAdmin(createSystemUserDto));
    Assertions.assertEquals("User with email exists", itemExistsException.getMessage());
  }

  @BeforeEach
  void setUp() {
    userService =
        new UserService(
            jwtAuthenticationManager, passwordEncoder, systemUserRepository, tokenProvider);
  }

  private void verifyNoMoreInteractions() {
    Mockito.verifyNoMoreInteractions(tokenProvider);
    Mockito.verifyNoMoreInteractions(systemUserRepository);
    Mockito.verifyNoMoreInteractions(passwordEncoder);
    Mockito.verifyNoMoreInteractions(jwtAuthenticationManager);
  }
}
