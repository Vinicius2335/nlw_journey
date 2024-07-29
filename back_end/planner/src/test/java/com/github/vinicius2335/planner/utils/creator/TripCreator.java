package com.github.vinicius2335.planner.utils.creator;

import com.github.vinicius2335.planner.modules.trip.Trip;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

import static com.github.vinicius2335.planner.utils.FakerCreator.FAKER;

@UtilityClass
public final class TripCreator {

    public static Trip mockTrip(){
        return Trip.builder()
                .destination(FAKER.address().city())
                .ownerName(FAKER.name().fullName())
                .ownerEmail(FAKER.internet().emailAddress())
                .startsAt(LocalDateTime.now())
                .endsAt(LocalDateTime.now().plusDays(10))
                .isConfirmed(false)
                .build();
    }

    public static Trip mockTripConfirmed(){
        return Trip.builder()
                .destination(FAKER.address().city())
                .ownerName(FAKER.name().fullName())
                .ownerEmail(FAKER.internet().emailAddress())
                .startsAt(LocalDateTime.now())
                .endsAt(LocalDateTime.now().plusDays(10))
                .isConfirmed(true)
                .build();
    }
}
