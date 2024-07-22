package com.github.vinicius2335.planner.modules.activity.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record ActivityDetailsDTO(
        UUID id,
        String title,
        LocalDateTime occursAt
) {
}
