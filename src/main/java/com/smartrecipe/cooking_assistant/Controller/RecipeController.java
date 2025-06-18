package com.smartrecipe.cooking_assistant.Controller;

import com.smartrecipe.cooking_assistant.Models.Recipe;
import com.smartrecipe.cooking_assistant.Models.UserFavoriteRecipe;
import com.smartrecipe.cooking_assistant.Services.RecipeService;
import com.smartrecipe.cooking_assistant.Services.UserFavoriteRecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserFavoriteRecipeService userFavoriteRecipeService;

    // ✅ Fetch all recipes
    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    // ✅ Fetch a specific recipe by ID
    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
        Recipe recipe = recipeService.getRecipeById(id);
        return recipe != null ? ResponseEntity.ok(recipe) : ResponseEntity.notFound().build();
    }

    // ✅ Save a new recipe
    @PostMapping
    public Recipe createRecipe(@RequestBody Recipe recipe) {
        return recipeService.saveRecipe(recipe);
    }

    // ✅ Update an existing recipe
    @PutMapping("/{id}")
    public ResponseEntity<Recipe> updateRecipe(@PathVariable Long id, @RequestBody Recipe updatedRecipe) {
        Recipe updated = recipeService.updateRecipe(id, updatedRecipe);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    // ✅ Delete a recipe
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Save a recipe as a favorite for a user
    @PostMapping("/favorite/{userId}/{recipeId}")
    public ResponseEntity<?> saveFavoriteRecipe(@PathVariable Long userId, @PathVariable Long recipeId) {
        boolean saved = userFavoriteRecipeService.saveFavoriteRecipe(userId, recipeId);
        return saved ? ResponseEntity.ok("✅ Recipe added to favorites!")
                : ResponseEntity.badRequest().body("❌ Recipe or User not found!");
    }

    // ✅ Fetch favorite recipes for a user
    @GetMapping("/favorites/{userId}")
    public ResponseEntity<List<Recipe>> getUserFavoriteRecipes(@PathVariable Long userId) {
        List<Recipe> favoriteRecipes = userFavoriteRecipeService.getUserFavoriteRecipes(userId);
        return favoriteRecipes.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(favoriteRecipes);
    }
}
