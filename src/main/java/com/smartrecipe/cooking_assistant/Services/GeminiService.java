package com.smartrecipe.cooking_assistant.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.*;

@Service
public class GeminiService {

     // Read API key from properties file
    private String apiKey="AIzaSyBjM6xzUgpwQipc90eOoiRCGeZ4uVxLcGg";

    private static final String MODEL_NAME = "models/gemini-1.5-pro-002";
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1/" + MODEL_NAME + ":generateContent?key=";

    public String generateRecipe(String ingredients) {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Construct the correct request payload
            Map<String, Object> requestBody = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(
                                    Map.of("text", "Suggest a recipe using: " + ingredients)
                            ))
                    )
            );

            String requestJson = objectMapper.writeValueAsString(requestBody);

            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create HTTP request
            HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

            // Make API call
            ResponseEntity<String> response = restTemplate.exchange(API_URL + apiKey, HttpMethod.POST, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                return extractRecipeText(response.getBody());
            } else {
                return "Error: " + response.getStatusCode();
            }
        } catch (Exception e) {
            return "Exception occurred: " + e.getMessage();
        }
    }

    private String extractRecipeText(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonResponse);

            JsonNode candidates = root.path("candidates");
            if (candidates.isArray() && candidates.size() > 0) {
                JsonNode parts = candidates.get(0).path("content").path("parts");
                if (parts.isArray() && parts.size() > 0) {
                    return parts.get(0).path("text").asText();
                }
            }
            return "No recipe found.";
        } catch (Exception e) {
            return "Error parsing API response.";
        }
    }
}
