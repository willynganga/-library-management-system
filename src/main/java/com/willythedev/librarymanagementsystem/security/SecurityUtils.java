package com.willythedev.librarymanagementsystem.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

public class SecurityUtils {
  private SecurityUtils() throws IllegalAccessException {
    throw new IllegalAccessException();
  }

  public static String getTokenFromRequest(HttpServletRequest httpServletRequest) {
    String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
    return StringUtils.hasLength(token) ? token : "";
  }
}
