package com.willythedev.librarymanagementsystem.security;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationManagerTest {
  private JwtAuthenticationManager jwtAuthenticationManager;
  @Mock private SystemUserDetailsService systemUserDetailsService;

  @Test
  void canAuthenticateTest() {
    String email = "test@test.com";
    Authentication authentication = new UsernamePasswordAuthenticationToken(email, "pass123456");
    User user =
        new User(email, "encrypted-password", List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    Mockito.when(systemUserDetailsService.loadUserByUsername(authentication.getName()))
        .thenReturn(user);

    Authentication authenticated = jwtAuthenticationManager.authenticate(authentication);

    ArgumentCaptor<String> nameArgumentCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(systemUserDetailsService).loadUserByUsername(nameArgumentCaptor.capture());

    Assertions.assertEquals("test@test.com", nameArgumentCaptor.getValue());
    Assertions.assertEquals("pass123456", authenticated.getCredentials());
  }

  @Test
  void cannotAuthenticateWithoutSecurityContextTest() {
    Authentication authentication =
        new UsernamePasswordAuthenticationToken("test@test.com", "pass123456");

    BadCredentialsException badCredentialsException =
        assertThrows(
            BadCredentialsException.class,
            () -> jwtAuthenticationManager.authenticate(authentication));

    Assertions.assertEquals(
        "Could not get username from token", badCredentialsException.getMessage());
  }

  @BeforeEach
  void setUp() {
    jwtAuthenticationManager = new JwtAuthenticationManager(systemUserDetailsService);
  }
}
