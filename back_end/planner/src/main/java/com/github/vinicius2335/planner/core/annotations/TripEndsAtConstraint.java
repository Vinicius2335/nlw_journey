package com.github.vinicius2335.planner.core.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE})
@Constraint(validatedBy = TripEndsAtConstraintValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface TripEndsAtConstraint {
    String message() default "Trip Ends_At: cannot be a past startsAt, empty or null";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
