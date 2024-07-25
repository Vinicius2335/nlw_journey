package com.github.vinicius2335.planner.modules.activity.dtos;

import jakarta.validation.constraints.NotBlank;

public record ActivityCreateRequest(
        @NotBlank(message = "Activity Title: cannot be null or empty")
        String title,

        @NotBlank(message = "Activity Ocurrs At: cannot be null or empty")
        String occursAt
) {
}
