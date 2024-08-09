package com.github.vinicius2335.planner.api.openapi;

import com.github.vinicius2335.planner.core.annotations.TripEndsAtConstraint;
import com.github.vinicius2335.planner.modules.email.EmailServiceException;
import com.github.vinicius2335.planner.modules.trip.Trip;
import com.github.vinicius2335.planner.modules.trip.dtos.TripCreateRequest;
import com.github.vinicius2335.planner.modules.trip.dtos.TripIdResponse;
import com.github.vinicius2335.planner.modules.trip.exceptions.TripNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Trip")
public interface TripControllerOpenApi {

    @Operation(
            summary = "Cria uma viagem", responses = {
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TripIdResponse.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Quando houver campos incorretos no corpo da requisição",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )),
    }
    )
    ResponseEntity<TripIdResponse> createTrip(
            @RequestBody(required = true, description = "Objeto que apresenta os campos necessário para criar uma nova viagem") @Valid TripCreateRequest request
    );

    @Operation(
            summary = "Busca detalhes de uma viagem", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Quando a operação for realizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Trip.class)
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "Quando a viagem não for encontrada pelo Id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    ))
    }
    )
    ResponseEntity<Trip> getTripDetails(
            @Parameter(required = true, description = "Identificador da viagem") UUID tripId
    ) throws TripNotFoundException;

    @Operation(
            summary = "Atualiza viagem", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Quando a operação for realizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Trip.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Quando houver campos incorretos no corpo da requisição",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "Quando a viagem não for encontrada pelo Id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    ))
    }
    )
    ResponseEntity<Trip> updateTrip(
            @Parameter(required = true, description = "Identificador da viagem") UUID tripId,
            @RequestBody(required = true, description = "Objeto que apresenta os campos necessários para atualizar uma viagem") @Valid @TripEndsAtConstraint TripCreateRequest request
    ) throws TripNotFoundException;

    @Operation(
            summary = "Confirma Viagem", responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Quando a operação for realizada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Trip.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "Quando ocorrer algum erro durante o envio de email",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )),
            @ApiResponse(
                    responseCode = "404",
                    description = "Quando a viagem não for encontrada pelo Id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    ))
    }
    )
    ResponseEntity<Trip> confirmTrip(
            @Parameter(required = true, description = "Identificador da viagem") UUID tripId
    ) throws TripNotFoundException, EmailServiceException;
}
