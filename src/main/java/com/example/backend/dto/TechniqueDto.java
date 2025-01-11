package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * DTO for {@link com.example.backend.models.Technique}
 */
@AllArgsConstructor
@Getter
@ToString
public class TechniqueDto implements Serializable {
    private final Long id;
    private final String name;
    private final String description;
    private final String videoURL;
    private final String imageURL;
}