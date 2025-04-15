package com.example.AIGen.security.jwt;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.example.AIGen.services.UserDetailsImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${aigen.app.jwtSecret}")
  private String jwtSecret;

  @Value("${aigen.app.jwtExpirationMs}")
  private int jwtExpirationMs;

 
  public String generateJwtToken(Authentication authentication) {

    UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
    logger.debug("Generating token for user: " + userPrincipal.getEmail());
    return Jwts.builder()
        .setSubject((userPrincipal.getEmail()))
        .setIssuedAt(new Date())
        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
        .signWith(key(), SignatureAlgorithm.HS256)
        .compact();
  }
  public String generateJwtToken(String email) {
      return Jwts.builder()
              .setSubject(email)
              .setIssuedAt(new Date())
              .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
              .signWith(key(), SignatureAlgorithm.HS256)
              .compact();
  }
  private Key key() {
      Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
      logger.debug("Using Secret Key: " + Base64.getEncoder().encodeToString(key.getEncoded()));
      return key;
  }

  public String getEmailFromJwtToken(String token) {
      Jws<Claims> claimsJws = Jwts.parserBuilder()
              .setSigningKey(key())
              .build()
              .parseClaimsJws(token);

      return claimsJws.getBody().getSubject();
  }
//  private Key key() {
//	  return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
//  }

  // Extract email from token
  public String getEmailFromJwtToken1(String token) {
      try {
          Claims claims = Jwts.parser()
                  .setSigningKey(jwtSecret)
                  .parseClaimsJws(token)
                  .getBody();

          return claims.get("email", String.class);
      } catch (Exception e) {
          System.err.println("Error parsing JWT token: " + e.getMessage());
          return null;
      }
  }

//  private Key key() {
//    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
//  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parserBuilder().setSigningKey(key()).build()
               .parseClaimsJws(token).getBody().getSubject();
  }


  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
      return true;
    } catch (SignatureException e) {
        logger.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
        logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
        logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
        logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
        logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }
}
