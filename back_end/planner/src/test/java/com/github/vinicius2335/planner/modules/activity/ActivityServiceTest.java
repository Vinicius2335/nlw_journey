package com.github.vinicius2335.planner.modules.activity;

import com.github.vinicius2335.planner.modules.activity.dtos.ActivityCreateRequest;
import com.github.vinicius2335.planner.modules.activity.dtos.ActivityIdResponse;
import com.github.vinicius2335.planner.modules.activity.dtos.ActivityListResponse;
import com.github.vinicius2335.planner.modules.trip.Trip;
import com.github.vinicius2335.planner.utils.creator.ActivityCreator;
import com.github.vinicius2335.planner.utils.creator.TripCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Teste Unitário para ActivityService")
class ActivityServiceTest {

    @InjectMocks
    private ActivityService underTest;

    @Mock
    private ActivityRepository activityRepository;

    private Activity activity;
    private Trip trip;

    @BeforeEach
    void setUp() {
        trip = TripCreator.mockTrip();
        trip.setId(UUID.randomUUID());
        activity = ActivityCreator.mockActivity(trip);
        activity.setId(UUID.randomUUID());

        when(activityRepository.save(any(Activity.class)))
                .thenReturn(activity);

        when(activityRepository.findAllByTripId(any(UUID.class)))
                .thenReturn(List.of(activity));
    }

    @Test
    @DisplayName("Registra uma nova atividade")
    void registerActivity_whenSuccessfully() {
        // given
        ActivityCreateRequest request = ActivityCreator.mockActivityCreateRequest(
                activity.getTitle(),
                activity.getOccursAt().toString()
        );
        // when
        assertThatCode(() -> underTest.registerActivity(request, trip))
                .doesNotThrowAnyException();
        // then
        verify(activityRepository, times(1))
                .save(any(Activity.class));
    }

    @Test
    @DisplayName("Lança uma exception quando a data da atividade for invalida")
    void registerActivity_throwsActivityOccursAtInvalidException_whenOccursAtIsInvalid() {
        // given
        ActivityCreateRequest invalidRequest = ActivityCreator.mockActivityCreateRequest(
                activity.getTitle(),
                trip.getEndsAt().plusDays(2).toString()
        );
        // when
        assertThatThrownBy(() -> underTest.registerActivity(invalidRequest, trip))
                .isInstanceOf(ActivityOccursAtInvalidException.class);
        // then
        verify(activityRepository, never())
                .save(any(Activity.class));
    }

    @Test
    @DisplayName("Recupera uma lista de atividades de uma viagem")
    void getAllActivitiesByTripId_returnActivityList_whenSuccessfull() {
        // when
        ActivityListResponse actual = underTest.getAllActivitiesByTripId(trip.getId());
        // then
        verify(activityRepository, times(1))
                .findAllByTripId(any(UUID.class));

        ActivityListResponse expected = ActivityCreator.mockActivityListResponse(activity);

        assertThat(actual)
                .isNotNull()
                .isEqualTo(expected);
    }
}