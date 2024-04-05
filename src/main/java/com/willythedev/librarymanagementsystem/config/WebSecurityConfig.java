package com.willythedev.librarymanagementsystem.config;

import com.willythedev.librarymanagementsystem.security.JwtAuthenticationFilter;
import com.willythedev.librarymanagementsystem.security.JwtAuthenticationManager;
import com.willythedev.librarymanagementsystem.security.SystemUserDetailsService;
import com.willythedev.librarymanagementsystem.security.UnauthorizedAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public JwtAuthenticationManager authenticationManager(
      SystemUserDetailsService systemUserDetailsService) {
    return new JwtAuthenticationManager(systemUserDetailsService);
  }

  @Bean
  public PasswordEncoder providePasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http, UnauthorizedAuthenticationEntryPoint unauthorizedAuthenticationEntryPoint)
      throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .sessionManagement(
            sessionManagementConfigurer ->
                sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    http.authorizeHttpRequests(
        requestMatcherRegistry ->
            requestMatcherRegistry
                .requestMatchers(
                    "/", "/api/v1/auth/login", "/api/v1/auth/admin", "/api/v1/auth/patron")
                .permitAll()
                .anyRequest()
                .authenticated());
    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    http.exceptionHandling(
        exceptionHandlingConfigurer ->
            exceptionHandlingConfigurer.authenticationEntryPoint(
                unauthorizedAuthenticationEntryPoint));

    return http.build();
  }
}
