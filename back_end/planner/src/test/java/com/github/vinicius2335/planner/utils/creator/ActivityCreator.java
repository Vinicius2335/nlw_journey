package com.github.vinicius2335.planner.utils.creator;

import com.github.vinicius2335.planner.modules.activity.Activity;
import com.github.vinicius2335.planner.modules.trip.Trip;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

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
}
