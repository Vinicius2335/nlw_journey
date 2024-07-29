package com.github.vinicius2335.planner.modules.participant;

import com.github.vinicius2335.planner.modules.email.EmailService;
import com.github.vinicius2335.planner.modules.email.EmailServiceException;
import com.github.vinicius2335.planner.modules.participant.dtos.ParticipantDetailsDTO;
import com.github.vinicius2335.planner.modules.participant.dtos.ParticipantIdResponse;
import com.github.vinicius2335.planner.modules.trip.Trip;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Log4j2
public class ParticitantService {
    private final ParticipantRepository participantRepository;
    private final EmailService emailService;

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
     * @throws EmailServiceException quando ocorrer algum erro durante o envio de email
     */
    @Transactional
    public ParticipantIdResponse registerParticipantToTrip(
            String email,
            Trip trip
    ) throws EmailServiceException {
        Participant participant = new Participant(email, trip);
        participantRepository.save(participant);

        if (trip.isConfirmed()) {
            triggerConfirmationEmailToParticipant(email, trip);
        }

        return new ParticipantIdResponse(participant.getId());
    }

    /**
     * Envia um email de confirmação da viagem para uma lista de participantes
     * @param trip viagem
     */
    public void triggerConfirmationEmailToParticipants(Trip trip) {
        List<Participant> participants = participantRepository.findByTripId(trip.getId());

        participants.forEach(participant -> {
            try {
                triggerConfirmationEmailToParticipant(participant.getEmail(), trip);
            } catch (EmailServiceException e) {
                log.error("Erro ao tentar enviar o email de confirmação para a lista de convidados da viagem: {}", e.getMessage());
            }
        });

    }

    /**
     * Envia um email de confirmação da viagem para um participante específico
     *
     * @param email do participante
     * @param trip viagem
     * @throws EmailServiceException quando ocorrer algum erro durante o envio de email
     */
    public void triggerConfirmationEmailToParticipant(String email, Trip trip) throws EmailServiceException {
        emailService.send(email, trip);
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

    /**
     * Retorna participante encontrado pelo id
     * @param participantId identificador do participante
     * @return {@code Participant} encontrado
     * @throws ParticipantNotFoundException quando participante não foi encontrado pelo {@code participantId}
     */
    public Participant getParticipantById(UUID participantId)
            throws ParticipantNotFoundException {
        return participantRepository.findById(participantId)
                .orElseThrow(() -> new ParticipantNotFoundException("Participante não foi encontrado pelo id: " + participantId));
    }

    /**
     * Salva um novo participante
     * @param participant a ser salvo
     * @return {@code Participant} salvo
     */
    public Participant saveParticipant(Participant participant){
        return participantRepository.save(participant);
    }
}
