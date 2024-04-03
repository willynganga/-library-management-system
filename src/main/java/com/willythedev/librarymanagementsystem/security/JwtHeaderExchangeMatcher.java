package com.willythedev.librarymanagementsystem.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class JwtHeaderExchangeMatcher implements RequestMatcher {
  @Override
  public boolean matches(HttpServletRequest request) {
    String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    return authorizationHeader != null;
  }

  @Override
  public MatchResult matcher(HttpServletRequest request) {
    String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    return authorizationHeader == null ? MatchResult.notMatch() : MatchResult.match();
  }
}
