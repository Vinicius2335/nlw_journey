package com.github.vinicius2335.planner.api.openapi;

import com.github.vinicius2335.planner.modules.email.EmailServiceException;
import com.github.vinicius2335.planner.modules.participant.dtos.ParticipantEmailRequest;
import com.github.vinicius2335.planner.modules.participant.dtos.ParticipantIdResponse;
import com.github.vinicius2335.planner.modules.participant.dtos.ParticipantListResponse;
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
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Tag(name = "Trip-Participant")
public interface TripParticipantsControllerOpenApi {

    @Operation(
            summary = "Convida um novo participante para à viagem", responses = {
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
            @ApiResponse(
                    responseCode = "400",
                    description = "Quando ocorrer um erro durante o envio de email",
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
    ResponseEntity<ParticipantIdResponse> inviteParticipant(
            @Parameter(required = true, description = "Identificador da viagem") UUID tripId,
            @RequestBody(required = true, description = "Objeto que apresenta o email necessário para convidar um participante") @Valid ParticipantEmailRequest request
    ) throws TripNotFoundException, EmailServiceException;

    @Operation(
            summary = "Retorna uma lista de atividades de uma viagem", responses = {
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TripIdResponse.class)
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
    ResponseEntity<ParticipantListResponse> getAllParticipants(
            @Parameter(required = true, description = "Identificador da viagem") UUID tripId
    ) throws TripNotFoundException;
}
