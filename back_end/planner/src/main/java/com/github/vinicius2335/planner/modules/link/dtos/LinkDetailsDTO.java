package com.github.vinicius2335.planner.modules.link.dtos;

import java.util.UUID;

public record LinkDetailsDTO(
        UUID id,
        String title,
        String url
) {
}
