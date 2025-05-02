package org.dvd.remixifyapi.recipe.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaginatedRecipeResponse {
    private List<RecipeDto> content;
    private int totalPages;
    private int currentPage;
    private long totalItems;
} 