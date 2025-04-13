package org.dvd.remixifyapi.auth.dto;

import org.dvd.remixifyapi.user.dto.UserDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDto {
    private String accessToken;
    private UserDto user;
}