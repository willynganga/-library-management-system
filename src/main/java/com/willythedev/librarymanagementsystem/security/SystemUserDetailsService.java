package com.willythedev.librarymanagementsystem.security;

import com.willythedev.librarymanagementsystem.model.SystemUser;
import com.willythedev.librarymanagementsystem.repository.SystemUserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SystemUserDetailsService implements UserDetailsService {
  private final SystemUserRepository systemUserRepository;

  public SystemUser findByEmail(String email) {
    return systemUserRepository.findByEmail(email).orElse(null);
  }

  public SystemUser saveUser(SystemUser user) {
    return systemUserRepository.save(user);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<SystemUser> userOptional = systemUserRepository.findByEmail(username);

    return userOptional
        .map(this::createSpringSecurityUser)
        .orElseThrow(() -> new BadCredentialsException("No user with provided username found!"));
  }

  private User createSpringSecurityUser(SystemUser user) {
    List<GrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority(user.getRole()));
    return new User(user.getEmail(), user.getPassword(), grantedAuthorities);
  }
}
