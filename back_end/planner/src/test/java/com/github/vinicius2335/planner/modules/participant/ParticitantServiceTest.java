package com.github.vinicius2335.planner.modules.participant;

import com.github.vinicius2335.planner.modules.email.EmailService;
import com.github.vinicius2335.planner.modules.email.EmailServiceException;
import com.github.vinicius2335.planner.modules.participant.dtos.ParticipantDetailsDTO;
import com.github.vinicius2335.planner.modules.trip.Trip;
import com.github.vinicius2335.planner.modules.trip.TripService;
import com.github.vinicius2335.planner.modules.trip.exceptions.TripNotFoundException;
import com.github.vinicius2335.planner.utils.creator.ParticipantCreator;
import com.github.vinicius2335.planner.utils.creator.TripCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Teste Unitário para ParticipantService")
class ParticitantServiceTest {

    @InjectMocks
    private ParticitantService underTest;

    @Mock
    private ParticipantRepository participantRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private TripService tripService;

    private Trip trip;
    private List<String> emailsToInvite;
    private Participant participant;

    @BeforeEach
    void setUp() throws EmailServiceException, TripNotFoundException {
        emailsToInvite = TripCreator.createEmailsToInviteList();
        trip = TripCreator.mockTrip();
        trip.setId(UUID.randomUUID());
        participant = ParticipantCreator.mockParticipant(trip);
        participant.setId(UUID.randomUUID());

        when(participantRepository.saveAll(anyCollection()))
                .thenReturn(List.of());

        when(participantRepository.save(any(Participant.class)))
                .thenReturn(participant);

        doNothing().when(emailService).send(anyString(),  any(Trip.class));

        when(participantRepository.findByTripId(any(UUID.class)))
                .thenReturn(List.of(participant));

        when(participantRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(participant));

        when(tripService.findTripById(any(UUID.class)))
                .thenReturn(trip);
    }

    @Test
    @DisplayName("Registra uma lista de participante à viagem")
    void registerParticipantsToTrip_whenSuccessfully() {
        // when
        assertThatCode(() -> underTest.registerParticipantsToTrip(emailsToInvite, trip))
                .doesNotThrowAnyException();
        // then
        verify(participantRepository, times(1))
                .saveAll(anyCollection());
    }

    @Test
    @DisplayName("Registra um participante à viagem")
    void registerParticipantToTrip_whenSuccessfully() {
        // config
        Participant participantToRegister = new Participant(participant.getEmail(), trip);
        // when
        assertThatCode(() -> underTest.registerParticipantToTrip(participant.getEmail(), trip))
                .doesNotThrowAnyException();
        // then
        verify(participantRepository, times(1)).save(participantToRegister);
    }

    @Test
    @DisplayName("Registra um participante à viagem e envia, se a viagem estiver confirmada, um email")
    void registerParticipantToTrip_sendEmailIfTripIsConfirmed_whenSuccessfully() throws EmailServiceException {
        // config
        Trip tripConfirmed = TripCreator.mockTripConfirmed();
        Participant participantToRegister = new Participant(participant.getEmail(), tripConfirmed);
        // when
        assertThatCode(() -> underTest.registerParticipantToTrip(participant.getEmail(), tripConfirmed))
                .doesNotThrowAnyException();
        // then
        verify(participantRepository, times(1)).save(participantToRegister);
        verify(emailService, times(1)).send(
                anyString(),
                any(Trip.class)
        );
    }

    @Test
    @DisplayName("Registra um participante à viagem, lança um exception quando ocorrer um erro durante o envio de email")
    void registerParticipantToTrip_throwsEmailServiceException_whenSendEmailFailed() throws EmailServiceException {
        // config
        Trip tripConfirmed = TripCreator.mockTripConfirmed();
        Participant participantToRegister = new Participant(participant.getEmail(), tripConfirmed);

        doThrow(EmailServiceException.class).when(emailService).send(anyString(), any(Trip.class));

        // when
        assertThatThrownBy(() -> underTest.registerParticipantToTrip(participant.getEmail(), tripConfirmed))
                .isInstanceOf(EmailServiceException.class);
        // then
        verify(participantRepository, times(1)).save(participantToRegister);
        verify(emailService, times(1)).send(
                anyString(),
                any(Trip.class)
        );
    }

    @Test
    @DisplayName("Aciona o email de confirmação para uma lista de participantes")
    void triggerConfirmationEmailToParticipants_sendEmails_whenSuccessfully() throws EmailServiceException {
        // when
        assertThatCode(() -> underTest.triggerConfirmationEmailToParticipants(trip))
                .doesNotThrowAnyException();
        // then
        verify(participantRepository, times(1)).findByTripId(trip.getId());
        verify(emailService, times(1)).send(
                anyString(),
                any(Trip.class)
        );
    }

    @Test
    @DisplayName("lança uma exception ao tentar enviar um email de confirmação para algum participante da lista")
    void triggerConfirmationEmailToParticipants_throwsEmailServiceException_whenSendEmailFailed() throws EmailServiceException {
        // config
        doThrow(EmailServiceException.class).when(emailService).send(anyString(), any(Trip.class));
        // when
        assertThatThrownBy(() -> underTest.triggerConfirmationEmailToParticipants(trip))
                .isInstanceOf(EmailServiceException.class);
        // then
        verify(participantRepository, times(1)).findByTripId(trip.getId());
        verify(emailService, times(1)).send(
                anyString(),
                any(Trip.class)
        );
    }

    @Test
    @DisplayName("Aciona o email de confirmação para um participante")
    void triggerConfirmationEmailToParticipant_senEmail_whenSuccessfully() throws EmailServiceException {
        // when
        assertThatCode(() -> underTest.triggerConfirmationEmailToParticipant(
                participant.getEmail(),
                trip
        )).doesNotThrowAnyException();
        // then
        verify(emailService, times(1))
                .send(anyString(), any(Trip.class));
    }

    @Test
    @DisplayName("lança uma exception ao tentar enviar um email")
    void triggerConfirmationEmailToParticipant_throwsEmailServiceException_whenSendEmailFailed() throws EmailServiceException {
        // config
        doThrow(EmailServiceException.class).when(emailService).send(anyString(), any(Trip.class));
        // when
        assertThatThrownBy(() -> underTest.triggerConfirmationEmailToParticipant(
                participant.getEmail(),
                trip
        )).isInstanceOf(EmailServiceException.class);
        // then
        verify(emailService, times(1))
                .send(anyString(), any(Trip.class));
    }

    @Test
    @DisplayName("Recupera todos os participantes registrado numa viagem")
    void getAllParticipantsByTripId_returnParticipantList_whenSuccessfully() throws TripNotFoundException {
        // config
        ParticipantDetailsDTO expected = ParticipantCreator.mockParticipantDetailsDTO(participant);
        // when
        List<ParticipantDetailsDTO> actual = underTest.getAllParticipantsByTripId(trip.getId());
        // then
        verify(tripService, times(1)).findTripById(trip.getId());
        verify(participantRepository, times(1))
                .findByTripId(any(UUID.class));

        assertThat(actual)
                .isNotNull()
                .hasSize(1)
                .contains(expected);
    }

    @Test
    @DisplayName("lança uma exception quando tripId não for encontrado")
    void getAllParticipantsByTripId_throwsTripNotFoundException_whenTripNotFoundById() throws TripNotFoundException {
        // config
        doThrow(TripNotFoundException.class).when(tripService).findTripById(trip.getId());
        // when
        assertThatThrownBy(() -> underTest.getAllParticipantsByTripId(trip.getId()))
                .isInstanceOf(TripNotFoundException.class);
        // then
        verify(tripService, times(1)).findTripById(trip.getId());
        verify(participantRepository, never())
                .findByTripId(any(UUID.class));

    }

    @Test
    @DisplayName("Recupera participante pelo Id")
    void getParticipantById_returnParticipant_whenSuccessfully() throws ParticipantNotFoundException {
        // when
        Participant actual = underTest.getParticipantById(participant.getId());
        // then
        verify(participantRepository, times(1)).findById(participant.getId());

        assertThat(actual)
                .isNotNull()
                .isEqualTo(participant);
    }

    @Test
    @DisplayName("Lança uma exception quando participante não for encontrado pelo id")
    void getParticipantById_throwsParticipantNotFoundException_whenParticipantNotFoundById() {
        // config
        when(participantRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());
        // when
        assertThatThrownBy(() -> underTest.getParticipantById(participant.getId()))
                .isInstanceOf(ParticipantNotFoundException.class);
        // then
        verify(participantRepository, times(1)).findById(participant.getId());
    }

    @Test
    @DisplayName("Salva e retorna um participante")
    void saveParticipant_returnParticipant_whenSuccessfully() {
        // when
        Participant actual = underTest.saveParticipant(participant);
        // then
        verify(participantRepository, times(1)).save(participant);

        assertThat(actual)
                .isNotNull()
                .isEqualTo(participant);
    }
}