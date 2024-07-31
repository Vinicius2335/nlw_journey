package com.github.vinicius2335.planner.utils.creator;

import com.github.vinicius2335.planner.modules.participant.Participant;
import com.github.vinicius2335.planner.modules.participant.dtos.ParticipantDetailsDTO;
import com.github.vinicius2335.planner.modules.trip.Trip;
import lombok.experimental.UtilityClass;

import static com.github.vinicius2335.planner.utils.creator.FakerCreator.FAKER;

@UtilityClass
public final class ParticipantCreator {

    public static Participant mockParticipant(Trip trip) {
        return Participant
                .builder()
                .name(FAKER.name().fullName())
                .email(FAKER.internet().emailAddress())
                .trip(trip)
                .isConfirmed(false)
                .build();
    }

    public static Participant mockParticipantConfirmed(Trip trip) {
        return Participant
                .builder()
                .name(FAKER.name().fullName())
                .email(FAKER.internet().emailAddress())
                .trip(trip)
                .isConfirmed(true)
                .build();
    }

    public static ParticipantDetailsDTO mockParticipantDetailsDTO(Participant participant){
        return new ParticipantDetailsDTO(
                participant.getId(),
                participant.getName(),
                participant.getEmail(),
                participant.isConfirmed()
        );
    }
}
