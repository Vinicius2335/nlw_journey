package com.github.vinicius2335.planner.modules.participant;

import com.github.vinicius2335.planner.modules.participant.dtos.ParticipantNameRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@CrossOrigin(maxAge = 3600, origins = "*")
@RequiredArgsConstructor
@RequestMapping("/participants")
@RestController
public class ParticipantController {
    private final ParticipantRepository participantRepository;

    /**
     * Endpoint responsável por confirmar o participante para a viagem
     * @param participantId identificador do participante
     * @param request Object que representa o nome do participante à ser confirmado
     * @return {@code Participant} confirmado
     */
    @PatchMapping("/{participantId}/confirm")
    public ResponseEntity<Participant> confirmParticipant(
            @PathVariable UUID participantId,
            @RequestBody @Valid ParticipantNameRequest request
    ){
        Optional<Participant> optParticipant = participantRepository.findById(participantId);

        if (optParticipant.isPresent()){
            Participant participant = optParticipant.get();
            participant.setConfirmed(true);
            participant.setName(request.name());

            participantRepository.save(participant);

            return ResponseEntity.ok(participant);
        }

        return ResponseEntity.notFound().build();
    }
}
