package com.github.vinicius2335.planner.modules.trip.dtos;

import com.github.vinicius2335.planner.core.annotations.TripEndsAtConstraint;
import com.github.vinicius2335.planner.core.annotations.TripStartsAtConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@TripEndsAtConstraint
public record TripCreateRequest(
        @NotBlank(message = "Trip Destination: cannot be null or empty")
        String destination,

        @TripStartsAtConstraint
        String startsAt,

        @NotBlank(message = "Trip Ends At: cannot be null or empty")
        String endsAt,

        @Size(min = 1, message = "Trip Emails To Invite: must have at least 1 registered email")
        List<@Email String> emailsToInvite,

        @NotBlank(message = "Trip Owner Name: cannot be null or empty")
        String ownerName,

        @NotBlank(message = "Trip Owner Email: cannot be null or empty")
        String ownerEmail
) {
}
