package com.github.vinicius2335.planner.modules.trip;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TripPayloadRequest(
        String destination,
        String startsAt,
        String endsAt,
        List<String> emailsToInvite,
        String ownerName,
        String ownerEmail
) {
}
