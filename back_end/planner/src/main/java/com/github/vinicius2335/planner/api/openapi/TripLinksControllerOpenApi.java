package com.github.vinicius2335.planner.api.openapi;

import com.github.vinicius2335.planner.modules.link.dtos.LinkCreateRequest;
import com.github.vinicius2335.planner.modules.link.dtos.LinkIdResponse;
import com.github.vinicius2335.planner.modules.link.dtos.LinkListResponse;
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

@Tag(name = "Trip-Link")
public interface TripLinksControllerOpenApi {

    @Operation(
            summary = "Adiciona um link à viagem", responses = {
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LinkIdResponse.class)
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
    ResponseEntity<LinkIdResponse> registerLink(
            @Parameter(required = true, description = "Identificador da viagem") UUID tripId,
            @RequestBody(required = true, description = "Objeto que apresenta os campos necessários para criar um novo link") @Valid LinkCreateRequest request
    ) throws TripNotFoundException;

    @Operation(
            summary = "Retorna todos os links relacionados à uma viagem", responses = {
            @ApiResponse(
                    responseCode = "201",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LinkListResponse.class)
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
    ResponseEntity<LinkListResponse> getAllLinks(
            @Parameter(required = true, description = "Identificador da viagem") UUID tripId
    ) throws TripNotFoundException;
}
