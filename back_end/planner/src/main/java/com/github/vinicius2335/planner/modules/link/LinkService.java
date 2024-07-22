package com.github.vinicius2335.planner.modules.link;

import com.github.vinicius2335.planner.modules.link.dtos.LinkCreateRequest;
import com.github.vinicius2335.planner.modules.link.dtos.LinkDetailsDTO;
import com.github.vinicius2335.planner.modules.link.dtos.LinkIdResponse;
import com.github.vinicius2335.planner.modules.link.dtos.LinkListResponse;
import com.github.vinicius2335.planner.modules.trip.Trip;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LinkService {
    private final LinkRepository linkRepository;

    /**
     * Cria um novo link, relacionado com uma viagem
     * @param request objeto que representa o link à ser criado
     * @param trip viagem que será vinculada ao link
     * @return {@code LinkIdResponse} id do link criado
     */
    @Transactional
    public LinkIdResponse registerLink(LinkCreateRequest request, Trip trip) {
        Link newLink = new Link(request, trip);

        linkRepository.save(newLink);

        return new LinkIdResponse(newLink.getId());
    }

    /**
     * Retorna todos links relacionadas a uma viagem
     * @param tripId identificador da viagem
     * @return {@code ActivityListResponse} objeto que representa uma lista com os detalhes de cada atividade encontrada
     */
    public LinkListResponse getAllLinksByTripId(UUID tripId) {
        List<LinkDetailsDTO> links = linkRepository
                .findAllByTripId(tripId)
                .stream()
                .map(link -> new LinkDetailsDTO(
                        link.getId(),
                        link.getTitle(),
                        link.getUrl()
                ))
                .collect(Collectors.toList());

        return new LinkListResponse(links);
    }
}
