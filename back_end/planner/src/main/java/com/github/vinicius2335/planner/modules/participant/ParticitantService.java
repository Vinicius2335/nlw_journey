package com.github.vinicius2335.planner.modules.participant;

import com.github.vinicius2335.planner.modules.participant.dtos.ParticipantDetailsDTO;
import com.github.vinicius2335.planner.modules.participant.dtos.ParticipantIdResponse;
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

    /**
     * Registra uma lista de participantes numa viagem
     * @param participantsToInvite Lista com os emails dos participantes que iram ser registrados na viagem
     * @param trip viagem onde os participantes serão cadastrados
     */
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

    /**
     * Registra um participante na viagem
     * @param email do participante
     * @param trip viagem onde o participante será registrado
     * @return {@code ParticipantIdResponse} objeto que representa o id do participante registrado
     */
    @Transactional
    public ParticipantIdResponse registerParticipantToTrip(
            String email,
            Trip trip
    ) {
        Participant participant = new Participant(email, trip);
        participantRepository.save(participant);

        if (trip.isConfirmed()) {
            triggerConfirmationEmailToParticipant(email);
        }

        return new ParticipantIdResponse(participant.getId());
    }

    /**
     * Envia um email de confirmação da viagem para uma lista de participantes
     * @param tripId identificador da viagem
     */
    public void triggerConfirmationEmailToParticipants(UUID tripId) {
        // NOTE = nada por enquanto
    }

    /**
     * Envia um email de confirmação da viagem para um participante específico
     * @param email do participante
     */
    public void triggerConfirmationEmailToParticipant(String email) {
        // NOTE - nada por enquanto
    }

    /**
     * Retorna todos os participantes que foram convidados para uma viagem
     * @param tripId identificador da viagem
     * @return {@code ParticipantDetailsDTO} objeto que representa a lista de participantes encontrado pelo id da viagem
     */
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
