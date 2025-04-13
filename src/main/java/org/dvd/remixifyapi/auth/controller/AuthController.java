package org.dvd.remixifyapi.auth.controller;

import java.util.Map;

import org.dvd.remixifyapi.auth.dto.AuthResponseDto;
import org.dvd.remixifyapi.auth.dto.ForgotPasswordRequest;
import org.dvd.remixifyapi.auth.dto.LoginRequestDto;
import org.dvd.remixifyapi.auth.dto.RegisterRequestDto;
import org.dvd.remixifyapi.auth.dto.ResetPasswordRequest;
import org.dvd.remixifyapi.auth.dto.TokenResponse;
import org.dvd.remixifyapi.auth.service.AuthService;
import org.dvd.remixifyapi.shared.security.jwt.JwtService;
import org.dvd.remixifyapi.user.dto.UserDto;
import org.dvd.remixifyapi.user.model.User;
import org.dvd.remixifyapi.user.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto registerRequest) {
        final TokenResponse token = authService.register(registerRequest);
        User user = userService.getUser(registerRequest.getUsername());

        AuthResponseDto response = AuthResponseDto.builder()
                .accessToken(token.getAccessToken())
                .user(UserDto.fromUser(user))
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        TokenResponse tokens = authService.login(loginRequest);

        User user = userService.getUser(jwtService.extractUsername(tokens.getAccessToken()));

        AuthResponseDto response = AuthResponseDto.builder()
                .accessToken(tokens.getAccessToken())
                .user(UserDto.fromUser(user))
                .build();

        // Create HTTP-only, secure cookie for refresh token
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokens.getRefreshToken())
                .httpOnly(true) // Prevents JavaScript access
                .secure(true) // Only sent over HTTPS
                .sameSite("Strict") // CSRF protection
                .path("/api/auth") // Only sent to auth endpoints
                .maxAge(30 * 24 * 60 * 60) // 30 days
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) {
        try {
            String username = jwtService.extractUsernameFromToken(authorization);
            User user = userService.getUser(username);
            return ResponseEntity.ok(UserDto.fromUser(user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(
            @CookieValue(name = "refreshToken") String refreshToken) {
        TokenResponse tokens = authService.refreshToken(refreshToken);
        User user = userService.getUser(jwtService.extractUsername(tokens.getAccessToken()));

        AuthResponseDto response = AuthResponseDto.builder()
                .accessToken(tokens.getAccessToken())
                .user(UserDto.fromUser(user))
                .build();

        ResponseCookie newRefreshTokenCookie = ResponseCookie.from("refreshToken", tokens.getRefreshToken())
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/api/auth")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, newRefreshTokenCookie.toString())
                .body(response);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie clearCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/api/auth")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, clearCookie.toString())
                .build();
    }

    @Operation(summary = "Initiate password reset", description = "Sends a password reset link to the provided email address")
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {
        try {
            authService.initiatePasswordReset(request.getEmail());
            return ResponseEntity.ok(Map.of("message", "Password reset email sent"));
        } catch (UsernameNotFoundException e) {
            // Always return 200 for security reasons
            return ResponseEntity
                    .ok(Map.of("message", "If an account exists with this email, a password reset link has been sent"));
        } catch (Exception e) {
            log.error("Error during password reset initiation", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An error occurred while processing your request"));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        try {
            authService.resetPassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Invalid or expired reset token"));
        } catch (Exception e) {
            log.error("Error during password reset", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "An error occurred while resetting your password"));
        }
    }

}
