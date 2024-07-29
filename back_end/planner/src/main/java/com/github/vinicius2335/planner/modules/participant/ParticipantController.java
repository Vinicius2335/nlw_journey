package com.github.vinicius2335.planner.modules.participant;

import com.github.vinicius2335.planner.modules.participant.dtos.ParticipantNameRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(maxAge = 3600, origins = "*")
@RequiredArgsConstructor
@RequestMapping("/participants")
@RestController
public class ParticipantController {
    private final ParticitantService particitantService;

    /**
     * Endpoint responsável por confirmar o participante para a viagem
     * @param participantId identificador do participante
     * @param request Object que representa o nome do participante à ser confirmado
     * @return {@code Participant} confirmado
     * @throws ParticipantNotFoundException quando participante não for encontrado pelo {@code participantId}
     */
    @PatchMapping("/{participantId}/confirm")
    public ResponseEntity<Participant> confirmParticipant(
            @PathVariable UUID participantId,
            @RequestBody @Valid ParticipantNameRequest request
    ) throws ParticipantNotFoundException {
        Participant participant = particitantService.getParticipantById(participantId);

        participant.setConfirmed(true);
        participant.setName(request.name());

        particitantService.saveParticipant(participant);

        return ResponseEntity.ok(participant);
    }
}
