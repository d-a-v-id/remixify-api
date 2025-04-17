package org.dvd.remixifyapi.user.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dvd.remixifyapi.recipe.model.Ingredient;
import org.dvd.remixifyapi.recipe.model.Recipe;
import org.dvd.remixifyapi.storage.util.FileStorageConstants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {

    // ─────────────────────────────────────────────────────
    // 📌 Basic Info
    // ─────────────────────────────────────────────────────

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must be at most 50 characters")
    @Column(nullable = false, length = 50)
    private String firstName;

    @Size(max = 50, message = "Last name must be at most 50 characters")
    @Column(length = 50)
    private String lastName;

    @Column(unique = true, nullable = false, length = 20)
    @NotBlank(message = "Username is required")
    @Pattern(
            regexp = "[a-zA-Z0-9_-]{3,20}",
            message =
                    "Username must be between 3 and 20 characters and contain only letters,"
                        + " numbers, underscores and hyphens")
    private String username;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Column(nullable = false)
    private String password;

    // ─────────────────────────────────────────────────────
    // 📌 Account Metadata
    // ─────────────────────────────────────────────────────

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.USER;

    @Builder.Default
    @Column(nullable = false)
    private Long createdAt = System.currentTimeMillis();

    @Builder.Default private String avatarPath = FileStorageConstants.DEFAULT_AVATAR_PATH;

    // ─────────────────────────────────────────────────────
    // 📌 Relationships
    // ─────────────────────────────────────────────────────

    @OneToMany(
            mappedBy = "author",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @Builder.Default
    private List<Recipe> recipes = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "recipe_likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id"))
    @Builder.Default
    private Set<Recipe> likedRecipes = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_preferred_ingredients",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    @Builder.Default
    private Set<Ingredient> preferredIngredients = new HashSet<>();

    // ─────────────────────────────────────────────────────
    // 📌 Utility Methods
    // ─────────────────────────────────────────────────────

    public String getFullName() {
        return (lastName != null && !lastName.isEmpty()) ? firstName + " " + lastName : firstName;
    }

    public void addPreferredIngredient(Ingredient ingredient) {
        this.preferredIngredients.add(ingredient);
        ingredient.getPreferredBy().add(this);
    }

    public void removePreferredIngredient(Ingredient ingredient) {
        this.preferredIngredients.remove(ingredient);
        ingredient.getPreferredBy().remove(this);
    }

    // ─────────────────────────────────────────────────────
    // 📌 Spring Security Overrides
    // ─────────────────────────────────────────────────────

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    // ─────────────────────────────────────────────────────
    // 📌 Enum
    // ─────────────────────────────────────────────────────

    public enum Role {
        USER,
        ADMIN
    }
}
