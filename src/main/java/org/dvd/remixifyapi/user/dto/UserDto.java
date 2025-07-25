package org.dvd.remixifyapi.user.dto;

import org.dvd.remixifyapi.storage.util.FileStorageUtils;
import org.dvd.remixifyapi.user.model.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String avatarPath;
    private int recipeCount;
    private Long createdAt;

    public static UserDto fromUser(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .avatarPath(FileStorageUtils.getFullAvatarImageUrl(user.getAvatarPath()))
                .recipeCount(user.getRecipes().size())
                .createdAt(user.getCreatedAt())
                .build();
    }

    public static User to(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .username(userDto.getUsername())
                .email(userDto.getEmail())
                .avatarPath(userDto.getAvatarPath())
                .build();
    }
}
