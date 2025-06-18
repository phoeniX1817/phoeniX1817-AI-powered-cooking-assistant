package com.smartrecipe.cooking_assistant.Controller;

import com.smartrecipe.cooking_assistant.Services.GeminiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai")
@CrossOrigin(origins = "*") // Allow all origins for testing
public class AIController {

    private final GeminiService geminiService;

    public AIController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @GetMapping("/recipe")
    public ResponseEntity<Map<String, Object>> getRecipe(@RequestParam String ingredients) {
        if (ingredients == null || ingredients.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Please provide at least one ingredient."));
        }

        try {
            String recipeText = geminiService.generateRecipe(ingredients);
            if (recipeText == null || recipeText.trim().isEmpty()) {
                return ResponseEntity.status(500).body(Map.of("error", "Failed to generate recipe."));
            }

            // ✅ Normalize line endings
            recipeText = recipeText.replace("\r\n", "\n").trim();

            // ✅ Extract sections based on keywords
            String title = "Generated Recipe";
            List<String> ingredientsList = new ArrayList<>();
            List<String> instructionsList = new ArrayList<>();

            String[] lines = recipeText.split("\n");
            boolean isIngredientsSection = false;
            boolean isInstructionsSection = false;

            for (String line : lines) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // ✅ Detect Title
                if (line.startsWith("#") || line.contains("Recipe for")) {
                    title = line.replace("#", "").trim();
                }

                // ✅ Detect Ingredients Section
                else if (line.toLowerCase().contains("ingredients:")) {
                    isIngredientsSection = true;
                    isInstructionsSection = false;
                    continue;
                }

                // ✅ Detect Instructions Section
                else if (line.toLowerCase().contains("instructions:") || line.toLowerCase().contains("method:")) {
                    isInstructionsSection = true;
                    isIngredientsSection = false;
                    continue;
                }

                // ✅ Store Ingredients
                if (isIngredientsSection && !line.matches("^\\d+\\.\\s.*")) {
                    ingredientsList.add(line.replace("*", "").trim()); // Remove bullet points
                }

                // ✅ Store Instructions (Ensure proper numbering)
                else if (isInstructionsSection || line.matches("^\\d+\\.\\s.*")) {
                    if (!line.matches("^\\d+\\.\\s.*")) { // If not numbered
                        instructionsList.add((instructionsList.size() + 1) + ". " + line);
                    } else {
                        instructionsList.add(line);
                    }
                }
            }

            // ✅ Return structured JSON
            Map<String, Object> response = new HashMap<>();
            response.put("title", title);
            response.put("ingredients", ingredientsList);
            response.put("instructions", instructionsList);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error generating recipe: " + e.getMessage()));
        }
    }
}
