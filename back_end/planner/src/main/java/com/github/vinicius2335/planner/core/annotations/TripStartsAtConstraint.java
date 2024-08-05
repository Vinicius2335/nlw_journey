package com.github.vinicius2335.planner.core.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Constraint(validatedBy = TripStartsAtConstraintValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface TripStartsAtConstraint {
    String message() default "Trip Starts_At: cannot be a past date, empty or null";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
