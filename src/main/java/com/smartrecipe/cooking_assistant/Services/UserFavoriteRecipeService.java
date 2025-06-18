package com.smartrecipe.cooking_assistant.Services;

import com.smartrecipe.cooking_assistant.Models.Recipe;
import com.smartrecipe.cooking_assistant.Models.UserFavoriteRecipe;
import com.smartrecipe.cooking_assistant.Repository.RecipeRepository;
import com.smartrecipe.cooking_assistant.Repository.UserFavoriteRecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserFavoriteRecipeService {

    @Autowired
    private UserFavoriteRecipeRepository userFavoriteRecipeRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    public boolean saveFavoriteRecipe(Long userId, Long recipeId) {
        if (recipeRepository.existsById(recipeId)) {
            UserFavoriteRecipe favorite = new UserFavoriteRecipe(userId, recipeId);
            userFavoriteRecipeRepository.save(favorite);
            return true;
        }
        return false;
    }

    public List<Recipe> getUserFavoriteRecipes(Long userId) {
        List<Long> favoriteRecipeIds = userFavoriteRecipeRepository.findByUserId(userId)
                .stream()
                .map(UserFavoriteRecipe::getRecipeId)
                .collect(Collectors.toList());

        return recipeRepository.findAllById(favoriteRecipeIds);
    }
}
