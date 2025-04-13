package org.dvd.remixifyapi.auth.service;

import java.util.HashMap;
import java.util.Map;

import org.dvd.remixifyapi.auth.dto.LoginRequestDto;
import org.dvd.remixifyapi.auth.dto.RegisterRequestDto;
import org.dvd.remixifyapi.auth.dto.TokenResponse;
import org.dvd.remixifyapi.email.service.EmailService;
import org.dvd.remixifyapi.shared.security.jwt.JwtService;
import org.dvd.remixifyapi.user.model.User;
import org.dvd.remixifyapi.user.repository.UserRepository;
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
        String refreshToken = jwtService.generateRefreshToken(savedUser);
        
        return new TokenResponse(jwtToken, refreshToken);
    }

    public TokenResponse login(LoginRequestDto loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()));

        var user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
                
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        
        return new TokenResponse(jwtToken, refreshToken);
    }

    public TokenResponse refreshToken(@NotNull String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);
        if (username == null) {
            throw new AuthenticationCredentialsNotFoundException("Invalid refresh token");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw new AuthenticationCredentialsNotFoundException("Invalid or expired refresh token");
        }

        String accessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

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
    }
}
