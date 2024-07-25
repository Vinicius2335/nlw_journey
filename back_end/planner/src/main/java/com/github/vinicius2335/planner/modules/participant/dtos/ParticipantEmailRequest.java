package com.github.vinicius2335.planner.modules.participant.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ParticipantEmailRequest(
        @Email(message = "Participant Email: must be a valid email")
        @NotBlank(message = "Participant Email: cannot be null or empty")
        String email
) {
}
