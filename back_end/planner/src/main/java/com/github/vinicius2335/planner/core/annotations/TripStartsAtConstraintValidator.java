package com.github.vinicius2335.planner.core.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TripStartsAtConstraintValidator implements ConstraintValidator<TripStartsAtConstraint, String> {
    @Override
    public boolean isValid(String startsAt, ConstraintValidatorContext context) {

        try {
            LocalDateTime startsAtDate = LocalDateTime.parse(startsAt, DateTimeFormatter.ISO_DATE_TIME);
            LocalDateTime now = LocalDateTime.now();

            // Quer dizer que:
            // startsAt para ser válido, não pode ser uma data anterior a data de criação da viagem
            return !startsAtDate.isBefore(now);
        } catch (NullPointerException ex){
            return false;
        }
    }
}
