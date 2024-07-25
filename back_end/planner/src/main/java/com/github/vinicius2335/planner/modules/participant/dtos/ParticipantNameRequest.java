package com.github.vinicius2335.planner.modules.participant.dtos;

import jakarta.validation.constraints.NotBlank;

public record ParticipantNameRequest(
        @NotBlank(message = "Participant Name: cannot be null or empty")
        String name
) {
}
