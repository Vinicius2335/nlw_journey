package com.github.vinicius2335.planner.modules.participant;

import java.util.List;

public record ParticipantListResponse(
        List<ParticipantDetailsDTO> participants
) {
}
