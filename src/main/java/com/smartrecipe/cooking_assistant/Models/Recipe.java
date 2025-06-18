package com.smartrecipe.cooking_assistant.Models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String ingredients; // Stored as a JSON string

    @Column(columnDefinition = "TEXT")
    private String instructions; // Stored as a long text
}
