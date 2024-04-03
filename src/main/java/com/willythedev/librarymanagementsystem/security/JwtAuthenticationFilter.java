package com.willythedev.librarymanagementsystem.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private static final String BEARER = "Bearer ";
  private static final UnaryOperator<String> isolateBearerValue =
      authValue -> authValue.substring(BEARER.length());
  private static final Predicate<String> matchBearerLength =
      authValue -> authValue.length() > BEARER.length();
  private final TokenProvider tokenProvider;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String bearerToken = SecurityUtils.getTokenFromRequest(request);

    if (matchBearerLength.test(bearerToken)) {
      String jwt = isolateBearerValue.apply(bearerToken);

      if (StringUtils.hasLength(jwt)) {
        Authentication authentication = tokenProvider.getAuthentication(jwt);

        if (authentication
            instanceof UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) {
          usernamePasswordAuthenticationToken.setDetails(
              new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }
    }

    filterChain.doFilter(request, response);
  }
}
