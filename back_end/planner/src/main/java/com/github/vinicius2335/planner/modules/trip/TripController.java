package com.github.vinicius2335.planner.modules.trip;

import com.github.vinicius2335.planner.modules.activity.ActivityCreateRequest;
import com.github.vinicius2335.planner.modules.activity.ActivityIdResponse;
import com.github.vinicius2335.planner.modules.activity.ActivityListResponse;
import com.github.vinicius2335.planner.modules.activity.ActivityService;
import com.github.vinicius2335.planner.modules.link.LinkCreateRequest;
import com.github.vinicius2335.planner.modules.link.LinkIdResponse;
import com.github.vinicius2335.planner.modules.link.LinkListResponse;
import com.github.vinicius2335.planner.modules.link.LinkService;
import com.github.vinicius2335.planner.modules.participant.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/trips")
@RestController
public class TripController {
    private final TripRepository tripRepository;
    private final ParticitantService particitantService;
    private final ActivityService activityService;
    private final LinkService linkService;

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

    // --------- Participants ---------

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
            @RequestBody ParticipantCreateRequest request
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

    // --------- Activitys ---------

    /**
     * Endpoint responsável por cadastrar uma atividade para a viagem
     * @param tripId identificador da viagem
     * @param request objeto que apresenta os campos necessários para criar uma nova atividade
     * @return {@code ActivityIdResponse} objeto que representa o id da atividade criada
     */
    @PostMapping("/{tripId}/activities")
    public ResponseEntity<ActivityIdResponse> registerActivity(
            @PathVariable UUID tripId,
            @RequestBody ActivityCreateRequest request
    ){
        Optional<Trip> optTrip = tripRepository.findById(tripId);

        if (optTrip.isPresent()){
            Trip trip = optTrip.get();
            ActivityIdResponse response = activityService.registerActivity(request, trip);

            return ResponseEntity
                    .ok(response);
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

    // --------- Links ---------

    /**
     * Endpoint responsável por criar um link para a viagem
     * @param tripId identificador da viagem
     * @param request objeto que apresenta os campos necessários para criar um novo link
     * @return {@code LinkIdResponse} objeto que representa o id do link criado
     */
    @PostMapping("/{tripId}/links")
    public ResponseEntity<LinkIdResponse> registerLink(
            @PathVariable UUID tripId,
            @RequestBody LinkCreateRequest request
    ){
        Optional<Trip> optTrip = tripRepository.findById(tripId);

        if (optTrip.isPresent()){
            Trip trip = optTrip.get();
            LinkIdResponse response = linkService.registerLink(request, trip);

            return ResponseEntity
                    .ok(response);
        }

        return ResponseEntity.notFound().build();
    }

    /**
     * Endpoint responsável por retornar todos os links relacionados com uma viagem
     * @param tripId identificador da viagem
     * @return {@code LinkListResponse} objeto que representa uma lista com os detalhes de todos os links encontrados
     */
    @GetMapping("/{tripId}/links")
    public ResponseEntity<LinkListResponse> getAllLinks(
            @PathVariable UUID tripId
    ){
        Optional<Trip> optTrip = tripRepository.findById(tripId);

        if (optTrip.isPresent()){
            Trip trip = optTrip.get();

            LinkListResponse response = linkService.getAllLinksByTripId(trip.getId());

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.notFound().build();
    }

}
