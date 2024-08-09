package com.github.vinicius2335.planner.api.openapi;

import com.github.vinicius2335.planner.modules.participant.Participant;
import com.github.vinicius2335.planner.modules.participant.ParticipantNotFoundException;
import com.github.vinicius2335.planner.modules.participant.dtos.ParticipantNameRequest;
import com.github.vinicius2335.planner.modules.trip.dtos.TripIdResponse;
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

@Tag(name = "Participant")
public interface ParticipantControllerOpenApi {

    @Operation(
            summary = "Confirma participação à uma viagem", responses = {
            @ApiResponse(
                    responseCode = "200",
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
            @ApiResponse(
                    responseCode = "404",
                    description = "Quando o participante não for encontrado pelo Id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    ))
    }
    )
    ResponseEntity<Participant> confirmParticipant(
            @Parameter(required = true, description = "Identificador da viagem") UUID tripId,
            @RequestBody(required = true, description = "Object que representa o nome do participante à ser confirmado") @Valid ParticipantNameRequest request
    ) throws ParticipantNotFoundException;
}
