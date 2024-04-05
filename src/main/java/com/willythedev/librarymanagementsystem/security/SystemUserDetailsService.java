package com.willythedev.librarymanagementsystem.security;

import com.willythedev.librarymanagementsystem.exception.CustomAuthenticationException;
import com.willythedev.librarymanagementsystem.model.SystemUser;
import com.willythedev.librarymanagementsystem.repository.SystemUserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SystemUserDetailsService implements UserDetailsService {
  private final PasswordEncoder passwordEncoder;
  private final SystemUserRepository systemUserRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<SystemUser> userOptional = systemUserRepository.findByEmail(username);

    if (userOptional.isEmpty()) {
      throw new CustomAuthenticationException("User does not exist");
    }

    SystemUser user = userOptional.get();

    if (user.isLocked()) {
      throw new CustomAuthenticationException("User account is locked");
    }

    String credentials =
        (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();

    if (!passwordEncoder.matches(credentials, user.getPassword())) {
      int trials = user.getTrials() + 1;
      user.setTrials(trials);
      user.setLocked(trials > 3);

      systemUserRepository.save(user);
      throw new CustomAuthenticationException("Provide correct email and password");
    }

    return userOptional
        .map(this::createSpringSecurityUser)
        .orElseThrow(
            () -> new CustomAuthenticationException("No user with provided username found!"));
  }

  private User createSpringSecurityUser(SystemUser user) {
    List<GrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority(user.getRole()));
    return new User(user.getEmail(), user.getPassword(), grantedAuthorities);
  }
}
