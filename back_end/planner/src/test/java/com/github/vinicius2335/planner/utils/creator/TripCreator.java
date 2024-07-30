package com.github.vinicius2335.planner.utils.creator;

import com.github.vinicius2335.planner.modules.trip.Trip;
import com.github.vinicius2335.planner.modules.trip.dtos.TripCreateRequest;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.github.vinicius2335.planner.utils.creator.FakerCreator.FAKER;

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

    public static TripCreateRequest mockTripCreateRequest(){
        return new TripCreateRequest(
                FAKER.address().city(),
                LocalDateTime.now().toString(),
                LocalDateTime.now().plusDays(10).toString(),
                createEmailsToInviteList(),
                FAKER.name().fullName(),
                FAKER.internet().emailAddress()
        );
    }

    public List<String> createEmailsToInviteList(){
        List<String> emailsToInvite = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            emailsToInvite.add(FAKER.internet().emailAddress());
        }

        return emailsToInvite;
    }
}
