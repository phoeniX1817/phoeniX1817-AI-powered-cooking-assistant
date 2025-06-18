package com.smartrecipe.cooking_assistant.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "user_favorite_recipes")
public class UserFavoriteRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // Stores which user saved this recipe
    private Long recipeId; // Stores which recipe is marked as favorite

    public UserFavoriteRecipe() {}

    public UserFavoriteRecipe(Long userId, Long recipeId) {
        this.userId = userId;
        this.recipeId = recipeId;
    }
}
