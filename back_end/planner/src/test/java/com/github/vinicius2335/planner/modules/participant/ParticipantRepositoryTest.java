package com.github.vinicius2335.planner.modules.participant;

import com.github.vinicius2335.planner.modules.trip.Trip;
import com.github.vinicius2335.planner.modules.trip.TripRepository;
import com.github.vinicius2335.planner.utils.creator.ParticipantCreator;
import com.github.vinicius2335.planner.utils.creator.TripCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Teste unitário para ParticipantRepository")
class ParticipantRepositoryTest {

    @Autowired
    private ParticipantRepository underTest;

    @Autowired
    private TripRepository tripRepository;

    private Trip trip;
    private Participant participant;

    @BeforeEach
    void setUp() {
        trip = tripRepository.save(TripCreator.mockTrip());
        participant = ParticipantCreator.mockParticipant(trip);
    }

    @Test
    @DisplayName("Salva e retorna participante")
    void save_returnParticipant_whenParticipantSaveSuccessfully(){
        // when
        Participant actual = underTest.save(participant);
        // then
        assertThat(actual)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(participant);
    }

    @Test
    @DisplayName("Sava todos os participantes e retorna uma lista")
    void saveAll_returnParticipantList_whenSuccessfully(){
        // given
        Participant participant2 = ParticipantCreator.mockParticipantConfirmed(trip);
        // when
        List<Participant> actual = underTest.saveAll(List.of(participant, participant2));
        // then
        assertThat(actual)
                .isNotNull()
                .hasSize(2);
    }

    private Participant seedDatabase(){
        return underTest.save(participant);
    }

    @Test
    @DisplayName("Procura participantes pelo ID da viagem, retorna uma lista de Participante quando encontrado")
    void findByTripId_returnParticipantList_whenSuccessfully() {
        // given
        Participant participantSaved = seedDatabase();
        UUID tripId = trip.getId();
        // when
        List<Participant> actual = underTest.findByTripId(tripId);
        // then
        assertThat(actual)
                .isNotNull()
                .hasSize(1)
                .contains(participantSaved);
    }

    @Test
    @DisplayName("Procura participantes pelo ID da viagem, retorna lista vazia quando ID da viagem não for encontrado")
    void findByTripId_returnEmptyList_whenTripIdNotFound(){
        // given
        UUID tripId = UUID.randomUUID();
        // when
        List<Participant> actual = underTest.findByTripId(tripId);
        // then
        assertThat(actual)
                .isEmpty();
    }

    @Test
    @DisplayName("Procura participante pelo ID, retorna um opcional de Participante quando encontrado")
    void findById_returnOptionalParticipant_whenSuccessfully(){
        // given
        Participant participantSaved = seedDatabase();
        UUID participantId = participantSaved.getId();
        // when
        Optional<Participant> actual = underTest.findById(participantId);
        // then
        assertThat(actual)
                .isNotNull()
                .isPresent()
                .hasValueSatisfying(
                        participantFound -> {
                            assertThat(participantFound)
                                    .isNotNull()
                                    .isEqualTo(participantSaved);
                        }
                );
    }

    @Test
    @DisplayName("Procura participante pelo ID, retorna um opcional vazio quando participante não encontrado")
    void findById_returnEmptyOptionalParticipant_whenParticipantNotFound(){
        // given
        UUID participantId = UUID.randomUUID();
        // whne
        Optional<Participant> actual = underTest.findById(participantId);
        // then
        assertThat(actual)
                .isNotNull()
                .isEmpty();
    }

}