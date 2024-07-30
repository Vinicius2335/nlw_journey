package com.github.vinicius2335.planner.modules.link;

import com.github.vinicius2335.planner.modules.trip.Trip;
import com.github.vinicius2335.planner.modules.trip.TripRepository;
import com.github.vinicius2335.planner.utils.creator.LinkCreator;
import com.github.vinicius2335.planner.utils.creator.TripCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@DisplayName("Teste Unitário para LinkRepository")
class LinkRepositoryTest {

    @Autowired
    private LinkRepository underTest;

    @Autowired
    private TripRepository tripRepository;

    private Link link;
    private Trip trip;

    @BeforeEach
    void setUp() {
        trip = tripRepository.save(TripCreator.mockTrip());
        link = LinkCreator.mockLink(trip);
    }

    @Test
    @DisplayName("Salva e retorna um link")
    void save_returnLink_whenSuccessfully(){
        // when
        Link actual = underTest.save(link);
        // then
        assertThat(actual)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(link);
    }

    private Link seedDatabase(){
        return underTest.save(link);
    }

    @Test
    @DisplayName("Procura Lista de links relacionada com uma viagem")
    void findAllByTripId_returnLinkList_whenLinksFoundByTripId() {
        // given
        Link linkSaved = seedDatabase();
        UUID tripId = trip.getId();
        // when
        List<Link> actual = underTest.findAllByTripId(tripId);
        // then
        assertThat(actual)
                .isNotNull()
                .hasSize(1)
                .contains(linkSaved);
    }

    @Test
    @DisplayName("Procura Lista de links relacionada com uma viagem, porem retorna uma lista vazia quando a viagem não possui nenhum link registrado")
    void findAllByTripId_returnEmptyLinkList_whenNotExistsLinkRelatedToTrip() {
        // given
        UUID tripId = trip.getId();
        // when
        List<Link> actual = underTest.findAllByTripId(tripId);
        // then
        assertThat(actual)
                .isNotNull()
                .isEmpty();
    }
}