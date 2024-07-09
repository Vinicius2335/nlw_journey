package com.github.vinicius2335.planner.modules.trip;

import com.github.vinicius2335.planner.modules.participant.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/trips")
@RestController
public class TripController {
    private final ParticitantService particitantService;
    private final TripRepository tripRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripPayloadRequest payload){
        Trip newTrip = new Trip(payload);

        tripRepository.save(newTrip);

        particitantService.registerParticipantsToTrip(payload.emailsToInvite(), newTrip);

        // TODO CREATED
        return ResponseEntity
                .ok(new TripCreateResponse(newTrip.getId()));
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID tripId){
        Optional<Trip> optTrip = tripRepository.findById(tripId);

        return optTrip.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    @PutMapping("/{tripId}")
    public ResponseEntity<Trip> updateTrip(
            @PathVariable UUID tripId,
            @RequestBody TripPayloadRequest request
    ){
        Optional<Trip> optTrip = tripRepository.findById(tripId);

        if (optTrip.isPresent()){
            Trip trip = optTrip.get();
            trip.updateTrip(request);

            tripRepository.save(trip);

            return ResponseEntity.ok(trip);
        }

        return ResponseEntity.notFound().build();
    }

    @Transactional
    @GetMapping("/{tripId}/confirm")
    public ResponseEntity<Trip> confirmTrip(
            @PathVariable UUID tripId
    ){
        Optional<Trip> optTrip = tripRepository.findById(tripId);

        if (optTrip.isPresent()){
            Trip trip = optTrip.get();
            trip.setConfirmed(true);

            tripRepository.save(trip);
            particitantService.triggerConfirmationEmailToParticipants(trip.getId());

            return ResponseEntity.ok(trip);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{tripId}/invite")
    public ResponseEntity<ParticipantCreateResponse> inviteParticipant(
            @PathVariable UUID tripId,
            @RequestBody ParticipantPayloadRequest payload
    ){
        Optional<Trip> optTrip = tripRepository.findById(tripId);

        if (optTrip.isPresent()){
            Trip trip = optTrip.get();

            ParticipantCreateResponse response = particitantService.registerParticipantToTrip(
                    payload.email(),
                    trip
            );

            if (trip.isConfirmed()) {
                particitantService.triggerConfirmationEmailToParticipant(payload.email());
            }

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{tripId}/participants")
    public ResponseEntity<ParticipantListResponse> getAllParticipants (
        @PathVariable UUID tripId
    ){
        Optional<Trip> optTrip = tripRepository.findById(tripId);

        if (optTrip.isPresent()){
            List<ParticipantDetailsDTO> participants = particitantService.getAllParticipantsByTripId(tripId);

            return ResponseEntity.ok(new ParticipantListResponse(participants));
        }

        return ResponseEntity.notFound().build();
    }
}
