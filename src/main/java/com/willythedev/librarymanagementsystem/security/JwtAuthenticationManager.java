package com.willythedev.librarymanagementsystem.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationManager implements AuthenticationManager {
  private final SystemUserDetailsService systemUserDetailsService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    if (authentication.isAuthenticated()) {
      return authentication;
    }

    UserDetails userDetails =
        authenticateToken((UsernamePasswordAuthenticationToken) authentication);

    if (userDetails == null) {
      throw new BadCredentialsException("Could not get username from token");
    }

    return new UsernamePasswordAuthenticationToken(
        authentication.getPrincipal(),
        authentication.getCredentials(),
        userDetails.getAuthorities());
  }

  private UserDetails authenticateToken(UsernamePasswordAuthenticationToken authenticationToken) {
    String username = authenticationToken.getName();

    if (username != null && SecurityContextHolder.getContext().getAuthentication() != null) {
      log.info("authenticating user " + username + ", setting security context");
      SecurityContextHolder.getContext().setAuthentication(authenticationToken);
      return systemUserDetailsService.loadUserByUsername(username);
    }

    return null;
  }
}
