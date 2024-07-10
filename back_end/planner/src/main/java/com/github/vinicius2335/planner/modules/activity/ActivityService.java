package com.github.vinicius2335.planner.modules.activity;

import com.github.vinicius2335.planner.modules.trip.Trip;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ActivityService {
    private final ActivityRepository activityRepository;

    /**
     * Cria uma nova atividade para a viagem
     *
     * @param request objeto que apresenta os campos necessário para criar uma nova atividade
     * @param trip    viagem se relacionará com a viagem criada
     * @return {@code ActivityIdResponse} objeto que representa o id da atividade criada
     */
    @Transactional
    public ActivityIdResponse registerActivity(ActivityCreateRequest request, Trip trip) {
        Activity newActivity = new Activity(request, trip);

        activityRepository.save(newActivity);

        return new ActivityIdResponse(newActivity.getId());
    }

    /**
     * Retorna todas as atividades relacionadas a uma viagem
     * @param tripId identificador da viagem
     * @return {@code ActivityListResponse} objeto que representa uma lista com os detalhes de cada atividade encontrada
     */
    public ActivityListResponse getAllActivitiesByTripId(UUID tripId) {
        List<ActivityDetailsDTO> activities = activityRepository
                .findAllByTripId(tripId)
                .stream()
                .map(activity -> new ActivityDetailsDTO(
                        activity.getId(),
                        activity.getTitle(),
                        activity.getOccursAt()
                ))
                .collect(Collectors.toList());

        return new ActivityListResponse(activities);
    }
}
