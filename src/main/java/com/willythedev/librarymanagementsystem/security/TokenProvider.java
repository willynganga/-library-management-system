package com.willythedev.librarymanagementsystem.security;

import com.willythedev.librarymanagementsystem.config.AppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@AllArgsConstructor
public class TokenProvider {
  public static final String AUTHORITIES = "roles";
  private final AppProperties appProperties;

  public String generateToken(Authentication authentication) {
    String authorities =
        authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpiry());

    return Jwts.builder()
        .setSubject(authentication.getName())
        .claim(AUTHORITIES, authorities)
        .setIssuedAt(new Date())
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
        .compact();
  }

  public Authentication getAuthentication(String token) {
    if (!StringUtils.hasLength(token) || isValidToken(token)) {
      throw new BadCredentialsException("Invalid token!");
    }

    Claims claims =
        Jwts.parser()
            .setSigningKey(appProperties.getAuth().getTokenSecret())
            .parseClaimsJws(token)
            .getBody();

    List<SimpleGrantedAuthority> authorities =
        Arrays.stream(claims.get(AUTHORITIES).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .toList();
    User principal = new User(claims.getSubject(), "", authorities);

    return new UsernamePasswordAuthenticationToken(principal, token, authorities);
  }

  public boolean isValidToken(String jwt) {
    try {
      Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(jwt);
      return false;
    } catch (SignatureException ex) {
      log.error("Invalid JWT signature");
    } catch (MalformedJwtException ex) {
      log.error("Invalid JWT token");
    } catch (ExpiredJwtException ex) {
      log.error("Expired JWT token");
    } catch (UnsupportedJwtException ex) {
      log.error("Unsupported JWT token");
    } catch (IllegalArgumentException ex) {
      log.error("JWT claims string is empty.");
    }
    return true;
  }
}
