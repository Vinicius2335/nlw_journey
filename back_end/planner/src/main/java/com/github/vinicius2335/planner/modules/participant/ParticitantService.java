package com.github.vinicius2335.planner.modules.participant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ParticitantService {

    public void registerParticipantsToTrip(
            List<String> participantsToInvite,
            UUID tripId
    ){

    }

    public void triggerConfirmationEmailToParticipants(UUID tripId){

    }
}
