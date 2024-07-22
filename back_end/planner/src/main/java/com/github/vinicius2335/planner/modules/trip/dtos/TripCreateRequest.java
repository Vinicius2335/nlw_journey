package com.github.vinicius2335.planner.modules.trip.dtos;

import java.util.List;

public record TripCreateRequest(
        String destination,
        String startsAt,
        String endsAt,
        List<String> emailsToInvite,
        String ownerName,
        String ownerEmail
) {
}
