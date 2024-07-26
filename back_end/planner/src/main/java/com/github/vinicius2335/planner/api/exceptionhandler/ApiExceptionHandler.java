package com.github.vinicius2335.planner.api.exceptionhandler;

import com.github.vinicius2335.planner.modules.activity.ActivityOccursAtInvalidException;
import com.github.vinicius2335.planner.modules.trip.exceptions.TripNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle("Um ou mais campos estão inválidos");

        Map<String, String> fields = ex.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        DefaultMessageSourceResolvable::getDefaultMessage
                ));

        if (ex.getObjectName().equalsIgnoreCase("tripCreateRequest")) {
            fields.put("endsAt", Objects.requireNonNull(ex.getDetailMessageArguments())[0].toString());
        }

        problemDetail.setProperty("fields", fields);

        return super.handleExceptionInternal(ex, problemDetail, headers, status, request);
    }

    @ExceptionHandler(TripNotFoundException.class)
    public ProblemDetail handleTripNotFound(TripNotFoundException ex){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Recurso não encontrada");
        problemDetail.setProperty("message", ex.getMessage());

        return problemDetail;
    }

    @ExceptionHandler(ActivityOccursAtInvalidException.class)
    public ProblemDetail handleActivityOccursAtInvalid(
            ActivityOccursAtInvalidException ex
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("A atividade ocorre em horário inválido");
        problemDetail.setProperty("message", ex.getMessage());

        return problemDetail;
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ProblemDetail handleDateTimeParse(
            DateTimeParseException ex
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Formato da data inválido");
        problemDetail.setProperty("message", ex.getMessage());
        problemDetail.setProperty("format", "yyyy-mm-ddThh:mm:ss.sssZ");
        problemDetail.setProperty("example", "2024-07-26T14:32:09.95Z");

        return problemDetail;
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(
            NoResourceFoundException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Endpoint não encontrado");
        problemDetail.setProperty("message", ex.getMessage());

        return super.handleExceptionInternal(ex, problemDetail, headers, status, request);
    }
}
