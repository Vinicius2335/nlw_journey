package com.github.vinicius2335.planner.core.annotations;

import com.github.vinicius2335.planner.modules.trip.dtos.TripCreateRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TripEndsAtConstraintValidator implements ConstraintValidator<TripEndsAtConstraint, TripCreateRequest> {
    @Override
    public boolean isValid(TripCreateRequest request, ConstraintValidatorContext context) {
        try {
            LocalDateTime startsAt = LocalDateTime.parse(request.startsAt(), DateTimeFormatter.ISO_DATE_TIME);
            LocalDateTime endsAt = LocalDateTime.parse(request.endsAt(), DateTimeFormatter.ISO_DATE_TIME);

            // Quer dizer que:
            // endsAt para ser válido, não pode ser uma data anterior a startsAt
            return !endsAt.isBefore(startsAt);
        } catch (NullPointerException ex){
            return false;
        }
    }
}
