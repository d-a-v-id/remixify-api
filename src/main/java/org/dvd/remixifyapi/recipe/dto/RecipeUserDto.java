package org.dvd.remixifyapi.recipe.dto;

import org.dvd.remixifyapi.user.model.User;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecipeUserDto {
    private Long id;
    private String username;

    public static RecipeUserDto fromUser(User user) {
        if (user == null) {
            return null;
        }
        return RecipeUserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .build();
    }
} 