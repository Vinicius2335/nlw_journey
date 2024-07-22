package com.github.vinicius2335.planner.modules.trip.controllers;

import com.github.vinicius2335.planner.modules.participant.ParticitantService;
import com.github.vinicius2335.planner.modules.trip.Trip;
import com.github.vinicius2335.planner.modules.trip.dtos.TripCreateRequest;
import com.github.vinicius2335.planner.modules.trip.dtos.TripIdResponse;
import com.github.vinicius2335.planner.modules.trip.TripRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@CrossOrigin(maxAge = 3600, origins = "*")
@RequiredArgsConstructor
@RequestMapping("/trips")
@RestController
public class TripController {
    private final TripRepository tripRepository;
    private final ParticitantService particitantService;

    /**
     * Endpoint responável por criar uma nova viagem
     *
     * @param request objeto que apresenta os campos necessário para criar uma nova viagem
     * @return {@code TripIdResponse} objeto que representa o id da viagem criada
     */
    @PostMapping
    @Transactional
    public ResponseEntity<TripIdResponse> createTrip(@RequestBody TripCreateRequest request) {
        Trip newTrip = new Trip(request);

        tripRepository.save(newTrip);

        particitantService.registerParticipantsToTrip(request.emailsToInvite(), newTrip);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new TripIdResponse(newTrip.getId()));
    }

    /**
     * Endpoint responsável por retornar os detalhes de uma viagem
     *
     * @param tripId identificador da viagem
     * @return Viagem encontrada
     */
    @GetMapping("/{tripId}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID tripId) {
        Optional<Trip> optTrip = tripRepository.findById(tripId);

        return optTrip.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint responsável por atualizar a viagem
     *
     * @param tripId  identificador da viagem
     * @param request objeto que apresenta os campos necessários para atualizar uma viagem
     * @return {@code Trip} - Viagem atualizada
     */
    @Transactional
    @PutMapping("/{tripId}")
    public ResponseEntity<Trip> updateTrip(
            @PathVariable UUID tripId,
            @RequestBody TripCreateRequest request
    ) {
        Optional<Trip> optTrip = tripRepository.findById(tripId);

        if (optTrip.isPresent()) {
            Trip trip = optTrip.get();
            trip.updateTrip(request);

            tripRepository.save(trip);

            return ResponseEntity.ok(trip);
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Endpoint responsável por confirmar uma viagem
     *
     * @param tripId identificador da viagem
     * @return {@code Trip} - Viagem criada
     */
    @Transactional
    @GetMapping("/{tripId}/confirm")
    public ResponseEntity<Trip> confirmTrip(
            @PathVariable UUID tripId
    ) {
        Optional<Trip> optTrip = tripRepository.findById(tripId);

        if (optTrip.isPresent()) {
            Trip trip = optTrip.get();
            trip.setConfirmed(true);

            tripRepository.save(trip);
            particitantService.triggerConfirmationEmailToParticipants(trip.getId());

            return ResponseEntity.ok(trip);
        }

        return ResponseEntity.notFound().build();
    }

}
