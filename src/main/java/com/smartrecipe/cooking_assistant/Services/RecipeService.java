package com.smartrecipe.cooking_assistant.Services;

import com.smartrecipe.cooking_assistant.Models.Recipe;
import com.smartrecipe.cooking_assistant.Repository.RecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipeService {
    @Autowired
    private RecipeRepository recipeRepository;

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id).orElse(null);
    }

    public Recipe saveRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }


    public Recipe updateRecipe(Long id, Recipe updatedRecipe) {
        return recipeRepository.findById(id).map(recipe -> {
            recipe.setTitle(updatedRecipe.getTitle());
            recipe.setIngredients(updatedRecipe.getIngredients());
            recipe.setInstructions(updatedRecipe.getInstructions());
            return recipeRepository.save(recipe);
        }).orElse(null);
    }
}
