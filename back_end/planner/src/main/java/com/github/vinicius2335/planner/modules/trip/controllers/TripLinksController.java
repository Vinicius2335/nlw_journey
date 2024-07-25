package com.github.vinicius2335.planner.modules.trip.controllers;

import com.github.vinicius2335.planner.modules.link.LinkService;
import com.github.vinicius2335.planner.modules.link.dtos.LinkCreateRequest;
import com.github.vinicius2335.planner.modules.link.dtos.LinkIdResponse;
import com.github.vinicius2335.planner.modules.link.dtos.LinkListResponse;
import com.github.vinicius2335.planner.modules.trip.Trip;
import com.github.vinicius2335.planner.modules.trip.TripService;
import com.github.vinicius2335.planner.modules.trip.exceptions.TripNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(maxAge = 3600, origins = "*")
@RequiredArgsConstructor
@RequestMapping("/trips")
@RestController
public class TripLinksController {
    private final LinkService linkService;
    private final TripService tripService;

    /**
     * Endpoint responsável por criar um link para a viagem
     * @param tripId identificador da viagem
     * @param request objeto que apresenta os campos necessários para criar um novo link
     * @return {@code LinkIdResponse} objeto que representa o id do link criado
     * @throws TripNotFoundException quando viagem nao for encontrado pelo {@code tripId}
     */
    @PostMapping("/{tripId}/links")
    public ResponseEntity<LinkIdResponse> registerLink(
            @PathVariable UUID tripId,
            @RequestBody @Valid LinkCreateRequest request
    ) throws TripNotFoundException {
        Trip trip = tripService.findTripById(tripId);
        LinkIdResponse response = linkService.registerLink(request, trip);

        return ResponseEntity
                .ok(response);
    }

    /**
     * Endpoint responsável por retornar todos os links relacionados com uma viagem
     * @param tripId identificador da viagem
     * @return {@code LinkListResponse} objeto que representa uma lista com os detalhes de todos os links encontrados
     * @throws TripNotFoundException quando viagem nao for encontrado pelo {@code tripId}
     */
    @GetMapping("/{tripId}/links")
    public ResponseEntity<LinkListResponse> getAllLinks(
            @PathVariable UUID tripId
    ) throws TripNotFoundException {
        Trip trip = tripService.findTripById(tripId);
        LinkListResponse response = linkService.getAllLinksByTripId(trip.getId());

        return ResponseEntity.ok(response);
    }
}
