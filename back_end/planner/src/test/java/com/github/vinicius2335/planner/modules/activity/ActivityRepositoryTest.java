package com.github.vinicius2335.planner.modules.activity;

import com.github.vinicius2335.planner.modules.trip.Trip;
import com.github.vinicius2335.planner.modules.trip.TripRepository;
import com.github.vinicius2335.planner.utils.creator.ActivityCreator;
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
@DisplayName("Teste Unitário para ActivityRepository")
class ActivityRepositoryTest {

    @Autowired
    private ActivityRepository underTest;

    @Autowired
    private TripRepository tripRepository;

    private Activity activity;
    private Trip trip;

    @BeforeEach
    void setUp() {
        trip = tripRepository.save(TripCreator.mockTrip());
        activity = ActivityCreator.mockActivity(trip);
    }

    @Test
    @DisplayName("Sava e retorna uma atividade")
    void save_returnActivitySaved_whenSuccessfully(){
        // when
        Activity actual = underTest.save(activity);
        // then
        assertThat(actual)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(activity);
    }

    private Activity seedDatabase(){
        return underTest.save(activity);
    }

    @Test
    @DisplayName("Procura e retorna uma lista de atividades relacionadas a uma viagem, quando encontrado")
    void findAllByTripId_returnActivityList_whenSuccessfully() {
        // given
        Activity activitySaved = seedDatabase();
        UUID tripId = trip.getId();
        // when
        List<Activity> actual = underTest.findAllByTripId(tripId);
        // then
        assertThat(actual)
                .isNotNull()
                .hasSize(1)
                .contains(activitySaved);
    }

    @Test
    @DisplayName("Procura e retorna uma lista de atividades vazia, relacionadas a uma viagem, quando nenhuma atividade foi cadastrada")
    void findAllByTripId_returnEmptyActivityList_whenNotExistsActivityRelatedToTrip() {
        // given
        UUID tripId = trip.getId();
        // when
        List<Activity> actual = underTest.findAllByTripId(tripId);
        // then
        assertThat(actual)
                .isNotNull()
                .isEmpty();
    }
}