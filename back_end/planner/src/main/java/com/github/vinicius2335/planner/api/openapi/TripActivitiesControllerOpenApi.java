package com.github.vinicius2335.planner.api.openapi;

import com.github.vinicius2335.planner.modules.activity.ActivityOccursAtInvalidException;
import com.github.vinicius2335.planner.modules.activity.dtos.ActivityCreateRequest;
import com.github.vinicius2335.planner.modules.activity.dtos.ActivityIdResponse;
import com.github.vinicius2335.planner.modules.activity.dtos.ActivityListResponse;
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

@Tag(name = "Trip-Activity")
public interface TripActivitiesControllerOpenApi {

    @Operation(
            summary = "Adiciona uma nova atividade à viagem", responses = {
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ActivityIdResponse.class)
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
    ResponseEntity<ActivityIdResponse> registerActivity(
            @Parameter(required = true, description = "Identificador da viagem") UUID tripId,
            @RequestBody(required = true, description = "Objeto que apresenta os campos necessários para criar uma nova atividade") @Valid ActivityCreateRequest request
    ) throws TripNotFoundException, ActivityOccursAtInvalidException;

    @Operation(
            summary = "Retorna uma lista de todas as atividades de uma viagem", responses = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ActivityListResponse.class)
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
    ResponseEntity<ActivityListResponse> getAllActivities(
            @Parameter(required = true, description = "Identificador da viagem") UUID tripId
    ) throws TripNotFoundException;
}
