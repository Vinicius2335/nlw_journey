package com.github.vinicius2335.planner.modules.participant;

import java.util.UUID;

public record ParticipantDetailsDTO(
        UUID id,
        String name,
        String email,
        boolean isConfirmed
) {
}
