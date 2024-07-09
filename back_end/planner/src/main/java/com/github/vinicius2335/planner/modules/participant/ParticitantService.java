package com.github.vinicius2335.planner.modules.participant;

import com.github.vinicius2335.planner.modules.trip.Trip;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ParticitantService {
    private final ParticipantRepository participantRepository;

    @Transactional
    public void registerParticipantsToTrip(
            List<String> participantsToInvite,
            Trip trip
    ) {
        List<Participant> participants = participantsToInvite.stream()
                .map(email -> new Participant(email, trip))
                .toList();

        participantRepository.saveAll(participants);
    }

    public void triggerConfirmationEmailToParticipants(UUID tripId) {

    }

    public void triggerConfirmationEmailToParticipant(String email) {

    }

    @Transactional
    public ParticipantCreateResponse registerParticipantToTrip(
            String email,
            Trip trip
    ) {
        Participant participant = new Participant(email, trip);
        participantRepository.save(participant);

        return new ParticipantCreateResponse(participant.getId());
    }

    public List<ParticipantDetailsDTO> getAllParticipantsByTripId(UUID tripId){
        return participantRepository.findByTripId(tripId)
                .stream()
                .map(participant -> new ParticipantDetailsDTO(
                        participant.getId(),
                        participant.getName(),
                        participant.getEmail(),
                        participant.isConfirmed()
                )).toList();
    }
}
