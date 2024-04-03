package com.willythedev.librarymanagementsystem.security;

import com.willythedev.librarymanagementsystem.model.SystemUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationManager implements AuthenticationManager {
  private final PasswordEncoder passwordEncoder;
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

    SystemUser user = systemUserDetailsService.findByEmail(userDetails.getUsername());

    if (user == null) {
      throw new BadCredentialsException("Could not authenticate user");
    }

    if (user.isLocked()) {
      throw new BadCredentialsException("User account is locked");
    }

    if (!passwordEncoder.matches((String) authentication.getCredentials(), user.getPassword())) {
      int trials = user.getTrials() + 1;
      user.setTrials(trials);
      user.setLocked(trials > 3);

      systemUserDetailsService.saveUser(user);
      throw new BadCredentialsException("Provide correct email and password");
    }

    SystemUser savedSystemUser = systemUserDetailsService.saveUser(user);
    return new UsernamePasswordAuthenticationToken(
        authentication.getPrincipal(),
        authentication.getCredentials(),
        List.of(new SimpleGrantedAuthority(savedSystemUser.getRole())));
  }

  private UserDetails authenticateToken(UsernamePasswordAuthenticationToken authenticationToken) {
    String username = authenticationToken.getName();

    log.info("checking authentication for user " + username);

    if (username != null && SecurityContextHolder.getContext().getAuthentication() != null) {
      log.info("authenticated user " + username + ", setting security context");
      return systemUserDetailsService.loadUserByUsername(username);
    }

    return null;
  }
}
