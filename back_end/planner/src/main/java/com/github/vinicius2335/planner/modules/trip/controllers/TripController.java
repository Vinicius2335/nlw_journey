package com.github.vinicius2335.planner.modules.trip.controllers;

import com.github.vinicius2335.planner.core.annotations.TripEndsAtConstraint;
import com.github.vinicius2335.planner.modules.participant.ParticitantService;
import com.github.vinicius2335.planner.modules.trip.Trip;
import com.github.vinicius2335.planner.modules.trip.TripService;
import com.github.vinicius2335.planner.modules.trip.dtos.TripCreateRequest;
import com.github.vinicius2335.planner.modules.trip.dtos.TripIdResponse;
import com.github.vinicius2335.planner.modules.trip.exceptions.TripNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(maxAge = 3600, origins = "*")
@RequiredArgsConstructor
@RequestMapping("/trips")
@RestController
@Validated
public class TripController {
    private final ParticitantService particitantService;
    private final TripService tripService;

    /**
     * Endpoint responável por criar uma nova viagem
     *
     * @param request objeto que apresenta os campos necessário para criar uma nova viagem
     * @return {@code TripIdResponse} objeto que representa o id da viagem criada
     */
    @PostMapping
    @Transactional
    public ResponseEntity<TripIdResponse> createTrip(
            @RequestBody @Valid TripCreateRequest request
    ) {
        Trip newTrip = tripService.createTrip(request);

        particitantService.registerParticipantsToTrip(request.emailsToInvite(), newTrip);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new TripIdResponse(newTrip.getId()));
    }

    /**
     * Endpoint responsável por retornar os detalhes de uma viagem
     * @param tripId tripId identificador da viagem
     * @return {@code Trip} - Viagem encontrada
     * @throws TripNotFoundException quando viagem nao for encontrado pelo {@code tripId}
     */
    @GetMapping("/{tripId}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID tripId) throws TripNotFoundException {
        return ResponseEntity.ok(tripService.findTripById(tripId));
    }

    /**
     * Endpoint responsável por atualizar a viagem
     *
     * @param tripId  identificador da viagem
     * @param request objeto que apresenta os campos necessários para atualizar uma viagem
     * @return {@code Trip} - Viagem atualizada
     * @throws TripNotFoundException quando viagem nao for encontrado pelo {@code tripId}
     */
    @Transactional
    @PutMapping("/{tripId}")
    public ResponseEntity<Trip> updateTrip(
            @PathVariable UUID tripId,
            @RequestBody @Valid @TripEndsAtConstraint TripCreateRequest request
    ) throws TripNotFoundException {
        return ResponseEntity.ok(tripService.updateTrip(tripId, request));
    }

    /**
     * Endpoint responsável por confirmar uma viagem
     *
     * @param tripId identificador da viagem
     * @return {@code Trip} - Viagem criada
     * @throws TripNotFoundException quando viagem nao for encontrado pelo {@code tripId}
     */
    @Transactional
    @GetMapping("/{tripId}/confirm")
    public ResponseEntity<Trip> confirmTrip(
            @PathVariable UUID tripId
    ) throws TripNotFoundException {
        Trip trip = tripService.confirmTrip(tripId);

        particitantService.triggerConfirmationEmailToParticipants(trip);

        return ResponseEntity.ok(trip);
    }

}
