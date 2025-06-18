package com.smartrecipe.cooking_assistant.Repository;

import com.smartrecipe.cooking_assistant.Models.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
