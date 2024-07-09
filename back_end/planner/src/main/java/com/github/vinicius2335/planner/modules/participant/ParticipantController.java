package com.github.vinicius2335.planner.modules.participant;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/participants")
@RestController
public class ParticipantController {
    private final ParticipantRepository participantRepository;

    @PostMapping("/{participantId}/confirm")
    public ResponseEntity<Participant> confirmParticipant(
            @PathVariable UUID participantId,
            @RequestBody ParticipantPayloadRequest payload
    ){
        Optional<Participant> optParticipant = participantRepository.findById(participantId);

        if (optParticipant.isPresent()){
            Participant participant = optParticipant.get();
            participant.setConfirmed(true);
            participant.setName(payload.name());

            participantRepository.save(participant);

            return ResponseEntity.ok(participant);
        }

        return ResponseEntity.notFound().build();
    }
}
