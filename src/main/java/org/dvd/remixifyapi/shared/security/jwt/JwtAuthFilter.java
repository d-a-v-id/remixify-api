package org.dvd.remixifyapi.shared.security.jwt;

import java.io.IOException;
import java.util.Optional;

import org.dvd.remixifyapi.auth.model.Token;
import org.dvd.remixifyapi.auth.repository.TokenRepository;
import org.dvd.remixifyapi.user.model.User;
import org.dvd.remixifyapi.user.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    /**
     * Internal filter method for JWT authentication.
     * This method processes the HTTP request to authenticate users based on JWT tokens.
     * The method performs the following checks in sequence:
     *      - Skips authentication for auth-related endpoints
     *      - Validates the presence and format of the Authorization header
     *      - Extracts and validates the JWT token
     *      - Verifies token existence and validity in the database
     *      - Authenticates the user if all checks pass
     *
     * @param request     The HTTP request to be processed
     * @param response    The HTTP response
     * @param filterChain The filter chain for continuing request processing
     * @throws ServletException If a servlet-specific error occurs
     * @throws IOException      If an I/O error occurs during request processing
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getServletPath().contains("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.debug("Auth header: {}", authHeader); // Add logging

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("No valid auth header found"); // Add logging
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = authHeader.substring(7);
        String username = jwtService.extractUsername(jwtToken);
        log.debug("Extracted username: {}", username); // Add logging

        if (username == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        Token token = tokenRepository.findByToken(jwtToken)
                .orElseThrow(() -> new IllegalArgumentException("Token not found"));
        if (token == null || token.isRevoked() || token.isExpired()) {
            filterChain.doFilter(request, response);
            return;
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean isTokenValid = jwtService.isTokenValid(jwtToken, user.get());
        if (!isTokenValid) {
            filterChain.doFilter(request, response);
            return;
        }

        var authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }

}
