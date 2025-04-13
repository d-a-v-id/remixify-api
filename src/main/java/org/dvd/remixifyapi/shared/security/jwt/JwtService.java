package org.dvd.remixifyapi.shared.security.jwt;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.dvd.remixifyapi.shared.config.AppProperties;
import org.dvd.remixifyapi.user.model.User;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final AppProperties appProperties; // Use AppProperties instead of JwtConfiguration

    public String generateToken(User user) {
        return buildToken(user, appProperties.getSecurity().getJwt().getExpirationTime());
    }

    public String generateRefreshToken(User user) {
        return buildToken(user, appProperties.getSecurity().getJwt().getRefreshToken().getExpirationTime());
    }

    private String buildToken(User user, Long expirationTime) {
        return Jwts.builder()
                .id(user.getId().toString())
                .claims(Map.of("name", user.getFullName()))
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSecretKey())
                .compact();
    }

    public String extractUsername(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(appProperties.getSecurity().getJwt().getSecretKey().getBytes());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getExpiration();
    }

    /* Forgot password */
    public String generatePasswordResetToken(User user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 900000)) // 15 minutes
                .signWith(getSecretKey())
                .compact();
    }

    public String validatePasswordResetToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSecretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.getSubject();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid or expired reset token");
        }
    }

    // Extract username from security context
    public String extractUsername() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null) {
            return authentication.getName();
        }
        throw new AuthenticationCredentialsNotFoundException("No authentication found in security context");
    }

    // Extract username directly from token
    public String extractUsernameFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return extractUsername(token);
    }

}
