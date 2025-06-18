package com.smartrecipe.cooking_assistant.Repository;

import com.smartrecipe.cooking_assistant.Models.UserFavoriteRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

    @Repository
    public interface UserFavoriteRecipeRepository extends JpaRepository<UserFavoriteRecipe, Long> {
        List<UserFavoriteRecipe> findByUserId(Long userId);
    }

