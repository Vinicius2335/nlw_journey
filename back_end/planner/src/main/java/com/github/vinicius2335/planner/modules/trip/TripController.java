package com.github.vinicius2335.planner.modules.trip;

import com.github.vinicius2335.planner.modules.participant.ParticitantService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        particitantService.registerParticipantsToTrip(payload.emailsToInvite(), newTrip.getId());

        return ResponseEntity
                .ok(new TripCreateResponse(newTrip.getId()));
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<Trip> getTripDetails(@PathVariable UUID tripId){
        Optional<Trip> trip = tripRepository.findById(tripId);

        return trip.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
