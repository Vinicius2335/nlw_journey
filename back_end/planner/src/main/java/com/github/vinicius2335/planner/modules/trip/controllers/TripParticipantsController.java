package com.github.vinicius2335.planner.modules.trip.controllers;

import com.github.vinicius2335.planner.modules.email.EmailServiceException;
import com.github.vinicius2335.planner.modules.participant.ParticitantService;
import com.github.vinicius2335.planner.modules.participant.dtos.ParticipantDetailsDTO;
import com.github.vinicius2335.planner.modules.participant.dtos.ParticipantEmailRequest;
import com.github.vinicius2335.planner.modules.participant.dtos.ParticipantIdResponse;
import com.github.vinicius2335.planner.modules.participant.dtos.ParticipantListResponse;
import com.github.vinicius2335.planner.modules.trip.Trip;
import com.github.vinicius2335.planner.modules.trip.TripService;
import com.github.vinicius2335.planner.modules.trip.exceptions.TripNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin(maxAge = 3600, origins = "*")
@RequiredArgsConstructor
@RequestMapping("/trips")
@RestController
public class TripParticipantsController {
    private final ParticitantService particitantService;
    private final TripService tripService;

    /**
     * Endpoint responável por convidar um participante para viagem
     *
     * @param tripId  identificador da viagem
     * @param request objeto que apresenta o email necessário para convidar um participante
     * @return {@code ParticipantCreateResponse} objeto que representa o id do participante convidado
     * @throws TripNotFoundException quando viagem nao for encontrado pelo {@code tripId}
     * @throws EmailServiceException quando ocorrer algum erro durante o envio de email
     */
    @PostMapping("/{tripId}/invite")
    public ResponseEntity<ParticipantIdResponse> inviteParticipant(
            @PathVariable UUID tripId,
            @RequestBody @Valid ParticipantEmailRequest request
    ) throws TripNotFoundException, EmailServiceException {
        Trip trip = tripService.findById(tripId);

        ParticipantIdResponse response = particitantService.registerParticipantToTrip(
                request.email(),
                trip
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Endpoint responsável por retornar uma lista de participante relacionados com uma viagem
     *
     * @param tripId identificador da viagem
     * @return {@code ParticipantListResponse} objeto que representa uma lista com os detalhes dos participantes encontrado
     * @throws TripNotFoundException quando viagem nao for encontrado pelo {@code tripId}
     */
    @GetMapping("/{tripId}/participants")
    public ResponseEntity<ParticipantListResponse> getAllParticipants(
            @PathVariable UUID tripId
    ) throws TripNotFoundException {
        tripService.findById(tripId);

        List<ParticipantDetailsDTO> participants = particitantService.getAllParticipantsByTripId(tripId);

        return ResponseEntity.ok(new ParticipantListResponse(participants));
    }
}
