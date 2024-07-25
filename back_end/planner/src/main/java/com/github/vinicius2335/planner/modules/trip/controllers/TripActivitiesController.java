package com.github.vinicius2335.planner.modules.trip.controllers;

import com.github.vinicius2335.planner.modules.activity.ActivityService;
import com.github.vinicius2335.planner.modules.activity.dtos.ActivityCreateRequest;
import com.github.vinicius2335.planner.modules.activity.dtos.ActivityIdResponse;
import com.github.vinicius2335.planner.modules.activity.dtos.ActivityListResponse;
import com.github.vinicius2335.planner.modules.trip.Trip;
import com.github.vinicius2335.planner.modules.trip.TripRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@CrossOrigin(maxAge = 3600, origins = "*")
@RequiredArgsConstructor
@RequestMapping("/trips")
@RestController
public class TripActivitiesController {
    private final TripRepository tripRepository;
    private final ActivityService activityService;

    /**
     * Endpoint responsável por cadastrar uma atividade para a viagem
     * @param tripId identificador da viagem
     * @param request objeto que apresenta os campos necessários para criar uma nova atividade
     * @return {@code ActivityIdResponse} objeto que representa o id da atividade criada
     */
    @PostMapping("/{tripId}/activities")
    public ResponseEntity<ActivityIdResponse> registerActivity(
            @PathVariable UUID tripId,
            @RequestBody @Valid ActivityCreateRequest request
    ){
        Optional<Trip> optTrip = tripRepository.findById(tripId);

        if (optTrip.isPresent()){
            Trip trip = optTrip.get();

            if (activityService.validateActivityCreateRequestFieldOccursAt(trip, request.occursAt())){
                ActivityIdResponse response = activityService.registerActivity(request, trip);

                return ResponseEntity
                        .ok(response);
            }

            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Endpoint responsável por retornar todas as atividades relacionadas a uma viagem
     * @param tripId identificador da viagem
     * @return {@code ActivityListResponse} objeto que representa uma lista com os detalhes das atividades relacionadas a viagem
     */
    @GetMapping("/{tripId}/activities")
    public ResponseEntity<ActivityListResponse> getAllActivities(
            @PathVariable UUID tripId
    ){
        Optional<Trip> optTrip = tripRepository.findById(tripId);

        if (optTrip.isPresent()){
            Trip trip = optTrip.get();
            ActivityListResponse response = activityService.getAllActivitiesByTripId(trip.getId());

            return ResponseEntity
                    .ok(response);
        }

        return ResponseEntity.notFound().build();
    }
}
