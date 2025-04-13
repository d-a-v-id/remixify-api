package org.dvd.remixifyapi.auth.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dvd.remixifyapi.auth.dto.LoginRequestDto;
import org.dvd.remixifyapi.auth.dto.RegisterRequestDto;
import org.dvd.remixifyapi.auth.dto.TokenResponse;
import org.dvd.remixifyapi.auth.model.Token;
import org.dvd.remixifyapi.auth.repository.TokenRepository;
import org.dvd.remixifyapi.email.service.EmailService;
import org.dvd.remixifyapi.shared.security.jwt.JwtService;
import org.dvd.remixifyapi.user.model.User;
import org.dvd.remixifyapi.user.repository.UserRepository;
import org.dvd.remixifyapi.user.service.UserService;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public TokenResponse register(RegisterRequestDto registerRequest) {
        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .email(registerRequest.getEmail())
                .build();

        User savedUser = userRepository.save(user);
        String jwtToken = jwtService.generateToken(savedUser);
        var refreshToken = jwtService.generateToken(savedUser);
        saveUserToken(savedUser, refreshToken);
        return new TokenResponse(jwtToken, refreshToken);
    }

    private void saveUserToken(User savedUser, String jwtToken) {
        var token = Token.builder()
                .user(savedUser)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    public TokenResponse login(LoginRequestDto loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        var user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, refreshToken);
        return new TokenResponse(jwtToken, refreshToken);
    }

    private void revokeAllUserTokens(final User user) {
        final List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (!validUserTokens.isEmpty()) {
            validUserTokens.forEach(token -> {
                token.setExpired(true);
                token.setRevoked(true);
            });
            tokenRepository.saveAll(validUserTokens);
        }
    }

    public TokenResponse refreshToken(@NotNull String refreshToken) {
        // No need to remove "Bearer " prefix - it's already just the token
        String username = jwtService.extractUsername(refreshToken);
        if (username == null) {
            throw new AuthenticationCredentialsNotFoundException("Invalid refresh token");
        }

        User user = userService.getUser(username);

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new AuthenticationCredentialsNotFoundException("Invalid or expired refresh token");
        }

        String accessToken = jwtService.generateToken(user);

        // You might want to generate a new refresh token each time for better security
        String newRefreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, newRefreshToken);

        return new TokenResponse(accessToken, newRefreshToken);
    }

    public void initiatePasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        String resetToken = jwtService.generatePasswordResetToken(user);
        String resetLink = String.format("http://localhost:5173/reset-password?token=%s", resetToken);

        Map<String, Object> templateVariables = new HashMap<>();
        templateVariables.put("firstName", user.getFirstName());
        templateVariables.put("resetLink", resetLink);

        emailService.sendTemplatedEmail(
                user.getEmail(),
                "🔑 Reset Your Password",
                "emails/password-reset-inline",
                templateVariables);
    }

    public void resetPassword(String token, String newPassword) {
        String username = jwtService.validatePasswordResetToken(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        revokeAllUserTokens(user);
    }
}
