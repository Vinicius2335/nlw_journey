package com.github.vinicius2335.planner.modules.trip;

import com.github.vinicius2335.planner.utils.creator.TripCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Teste Unitário para TripRepository")
class TripRepositoryTest {

    @Autowired
    private TripRepository underTest;

    private Trip trip;

    @BeforeEach
    void setUp() {
        trip = TripCreator.mockTrip();
    }

    @Test
    @DisplayName("Salva e retorna viagem")
    void save_returnTrip_whenTripSaveSuccessfully() {
        //when
        Trip actual = underTest.save(trip);
        // then
        assertThat(actual)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(trip);
    }

    /**
     * Utility method, insere uma viagem no banco de dados para testar funcionalidades que realizam alguma operação com uma viagem já registrada.
     *
     * @return Viagem criada
     */
    private Trip seedDatabase() {
        return underTest.save(trip);
    }

    @Test
    @DisplayName("Procura viagem pelo ID, retorna uma viagem quando encontrado")
    void findTripById_returnOptionalTrip_whenTripFound() {
        //given
        Trip tripSaved = seedDatabase();
        UUID tripId = tripSaved.getId();
        //when
        Optional<Trip> actual = underTest.findById(tripId);
        //then
        assertThat(actual)
                .isNotNull()
                .isPresent()
                .hasValueSatisfying(
                        tripFound -> assertThat(tripFound)
                                .isNotNull()
                                .isEqualTo(tripSaved)
                );
    }

    @Test
    @DisplayName("Procura viagem pelo ID, retorna um opcional vazio quando nada for encontrado")
    void findById_returnEmptyOptionalTrip_whenTripNotFound(){
        // given
        UUID tripId = UUID.randomUUID();
        // when
        Optional<Trip> actual = underTest.findById(tripId);
        // then
        assertThat(actual)
                .isNotNull()
                .isEmpty();
    }
}