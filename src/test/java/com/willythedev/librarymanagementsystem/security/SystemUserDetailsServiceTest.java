package com.willythedev.librarymanagementsystem.security;

import static org.junit.jupiter.api.Assertions.*;

import com.willythedev.librarymanagementsystem.exception.CustomAuthenticationException;
import com.willythedev.librarymanagementsystem.model.SystemUser;
import com.willythedev.librarymanagementsystem.repository.SystemUserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class SystemUserDetailsServiceTest {
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private SystemUserRepository systemUserRepository;
  private SystemUserDetailsService systemUserDetailsService;

  @BeforeEach
  void setUp() {
    systemUserDetailsService = new SystemUserDetailsService(passwordEncoder, systemUserRepository);
  }

  @Test
  void cannotLoadUserThatDoesNotExistTest() {
    String username = "test@test.com";

    Mockito.when(systemUserRepository.findByEmail(username)).thenReturn(Optional.empty());

    CustomAuthenticationException customAuthenticationException =
        assertThrows(
            CustomAuthenticationException.class,
            () -> systemUserDetailsService.loadUserByUsername(username));
    Assertions.assertEquals("User does not exist", customAuthenticationException.getMessage());
  }

  @Test
  void cannotLoadUserThatIsLockedTest() {
    String username = "test@test.com";
    SystemUser systemUser =
        SystemUser.builder()
            .email(username)
            .password("encrypted-password")
            .role("ROLE_ADMIN")
            .locked(true)
            .build();

    Mockito.when(systemUserRepository.findByEmail(username)).thenReturn(Optional.of(systemUser));

    CustomAuthenticationException badCredentialsException =
        assertThrows(
            CustomAuthenticationException.class,
            () -> systemUserDetailsService.loadUserByUsername(username));
    Assertions.assertEquals("User account is locked", badCredentialsException.getMessage());
  }

  @Test
  void cannotLoadUserWithWrongPasswordIsTest() {
    String username = "test@test.com";
    SystemUser systemUser =
        SystemUser.builder()
            .email(username)
            .password("encrypted-password")
            .role("ROLE_ADMIN")
            .locked(false)
            .build();
    Authentication authentication = new UsernamePasswordAuthenticationToken(username, "pass123456");
    SecurityContextHolder.getContext().setAuthentication(authentication);

    Mockito.when(systemUserRepository.findByEmail(username)).thenReturn(Optional.of(systemUser));
    Mockito.when(systemUserRepository.save(ArgumentMatchers.any())).thenReturn(systemUser);

    CustomAuthenticationException badCredentialsException =
        assertThrows(
            CustomAuthenticationException.class,
            () -> systemUserDetailsService.loadUserByUsername(username));

    ArgumentCaptor<SystemUser> systemUserArgumentCaptor = ArgumentCaptor.forClass(SystemUser.class);
    Mockito.verify(systemUserRepository).save(systemUserArgumentCaptor.capture());

    SystemUser systemUserArgumentCaptorValue = systemUserArgumentCaptor.getValue();
    Assertions.assertEquals(1, systemUserArgumentCaptorValue.getTrials());
    Assertions.assertFalse(systemUserArgumentCaptorValue.isLocked());
    Assertions.assertEquals(
        "Provide correct email and password", badCredentialsException.getMessage());
  }
}
