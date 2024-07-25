package com.github.vinicius2335.planner.modules.trip.controllers;

import com.github.vinicius2335.planner.modules.participant.ParticitantService;
import com.github.vinicius2335.planner.modules.participant.dtos.ParticipantEmailRequest;
import com.github.vinicius2335.planner.modules.participant.dtos.ParticipantDetailsDTO;
import com.github.vinicius2335.planner.modules.participant.dtos.ParticipantIdResponse;
import com.github.vinicius2335.planner.modules.participant.dtos.ParticipantListResponse;
import com.github.vinicius2335.planner.modules.trip.Trip;
import com.github.vinicius2335.planner.modules.trip.TripRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(maxAge = 3600, origins = "*")
@RequiredArgsConstructor
@RequestMapping("/trips")
@RestController
public class TripParticipantsController {
    private final TripRepository tripRepository;
    private final ParticitantService particitantService;

    /**
     * Endpoint responável por convidar um participante para viagem
     *
     * @param tripId  identificador da viagem
     * @param request objeto que apresenta o email necessário para convidar um participante
     * @return {@code ParticipantCreateResponse} objeto que representa o id do participante convidado
     */
    @PostMapping("/{tripId}/invite")
    public ResponseEntity<ParticipantIdResponse> inviteParticipant(
            @PathVariable UUID tripId,
            @RequestBody @Valid ParticipantEmailRequest request
    ) {
        Optional<Trip> optTrip = tripRepository.findById(tripId);

        if (optTrip.isPresent()) {
            Trip trip = optTrip.get();

            ParticipantIdResponse response = particitantService.registerParticipantToTrip(
                    request.email(),
                    trip
            );

            if (trip.isConfirmed()) {
                particitantService.triggerConfirmationEmailToParticipant(request.email());
            }

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Endpoint responsável por retornar uma lista de participante relacionados com uma viagem
     *
     * @param tripId identificador da viagem
     * @return {@code ParticipantListResponse} objeto que representa uma lista com os detalhes dos participantes encontrado
     */
    @GetMapping("/{tripId}/participants")
    public ResponseEntity<ParticipantListResponse> getAllParticipants(
            @PathVariable UUID tripId
    ) {
        Optional<Trip> optTrip = tripRepository.findById(tripId);

        if (optTrip.isPresent()) {
            List<ParticipantDetailsDTO> participants = particitantService.getAllParticipantsByTripId(tripId);

            return ResponseEntity.ok(new ParticipantListResponse(participants));
        }

        return ResponseEntity.notFound().build();
    }
}
