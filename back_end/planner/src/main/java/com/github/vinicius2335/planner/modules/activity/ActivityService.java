package com.github.vinicius2335.planner.modules.activity;

import com.github.vinicius2335.planner.modules.activity.dtos.*;
import com.github.vinicius2335.planner.modules.trip.Trip;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

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
     * @throws ActivityOccursAtInvalidException quando horário da atividade for inválido
     */
    @Transactional
    public ActivityIdResponse registerActivity(ActivityCreateRequest request, Trip trip) throws ActivityOccursAtInvalidException {
        validateActivityCreateRequestFieldOccursAt(trip, request.occursAt());

        Activity newActivity = new Activity(request, trip);

        activityRepository.save(newActivity);

        return new ActivityIdResponse(newActivity.getId());
    }

    /**
     * Retorna todas as atividades relacionadas a uma viagem
     *
     * @param tripId identificador da viagem
     * @return {@code ActivityListResponse} objeto que representa uma lista com os detalhes de cada atividade encontrada
     */
    public ActivityListResponse getAllActivitiesByTripId(UUID tripId) {
        List<Activity> activityList = activityRepository.findAllByTripId(tripId)
                .stream()
                .sorted(Comparator.comparing(Activity::getOccursAt))
                .toList();

        List<ActivitiesDTO> activities = new ArrayList<>();

        for (Activity activity : activityList) {

            boolean hasDateActivityRegistered = activities.stream()
                    .anyMatch(activitiesDTO -> activitiesDTO.getDate().getDayOfYear() == activity.getOccursAt().getDayOfYear());

            if (!hasDateActivityRegistered) {
                ActivitiesDTO activitiesDTO = new ActivitiesDTO(activity.getOccursAt(), new ArrayList<>());

                for (Activity compareActivity : activityList) {
                    if (
                            activity.getOccursAt().getDayOfYear() == compareActivity.getOccursAt().getDayOfYear()
                    ) {
                        activitiesDTO.getActivities().add(
                                new ActivityDetailsDTO(
                                        compareActivity.getId(),
                                        compareActivity.getTitle(),
                                        compareActivity.getOccursAt()
                                )
                        );
                    }
                }

                activities.add(activitiesDTO);
            }
        }

        return new ActivityListResponse(activities);
    }

    /**
     * Valida o data/hora de ocorrencia da atividade
     * @param trip identificador da viagem
     * @param occursAt horario da atividade
     * @throws ActivityOccursAtInvalidException quando {@code occursAt} for inválido
     */
    private void validateActivityCreateRequestFieldOccursAt(
            Trip trip,
            String occursAt
    ) throws ActivityOccursAtInvalidException {
        LocalDateTime activityOccursAtTime = LocalDateTime.parse(occursAt, DateTimeFormatter.ISO_DATE_TIME);

        // valida se a atividade ocorre entre o tempo de inicio e fim da viagem
        boolean isActivityOccursValid = !activityOccursAtTime.isBefore(trip.getStartsAt())
                && !activityOccursAtTime.isAfter(trip.getEndsAt());

        String pattern = "dd/MM/yyyy hh:mm";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);

        if (!isActivityOccursValid) {
            throw new ActivityOccursAtInvalidException("Data/Hora para que a atividade ocorra é inválido, deve ser entre " + trip.getStartsAt().format(dtf) + " até " + trip.getEndsAt().format(dtf));
        }
    }
}
