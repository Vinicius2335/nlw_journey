package com.github.vinicius2335.planner.utils.creator;

import com.github.vinicius2335.planner.modules.activity.Activity;
import com.github.vinicius2335.planner.modules.activity.dtos.*;
import com.github.vinicius2335.planner.modules.trip.Trip;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.github.vinicius2335.planner.utils.creator.FakerCreator.FAKER;

@UtilityClass
public final class ActivityCreator {

    public static Activity mockActivity(Trip trip) {
        return Activity.builder()
                .title(FAKER.esports().game())
                .occursAt(LocalDateTime.now().plusDays(1))
                .trip(trip)
                .build();
    }

    public static ActivityCreateRequest mockActivityCreateRequest(
            String title,
            String occursAt
    ){
        return new ActivityCreateRequest(
                title,
                occursAt
        );
    }

    public static ActivityCreateRequest mockInvalidActivityCreateRequest(){
        return new ActivityCreateRequest(null, null);
    }

    private static ActivitiesDTO moackActivitiesDTO(Activity activity){
        return new ActivitiesDTO(
                activity.getOccursAt(),
                List.of(new ActivityDetailsDTO(
                        activity.getId(),
                        activity.getTitle(),
                        activity.getOccursAt()
                ))
        );
    }

    public static ActivityListResponse mockActivityListResponse(Activity activity){
        return new ActivityListResponse(List.of(moackActivitiesDTO(activity)));
    }
}
