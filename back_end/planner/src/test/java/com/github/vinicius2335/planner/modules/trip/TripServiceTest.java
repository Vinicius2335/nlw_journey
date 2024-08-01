package com.github.vinicius2335.planner.modules.trip;

import com.github.vinicius2335.planner.modules.trip.dtos.TripCreateRequest;
import com.github.vinicius2335.planner.modules.trip.exceptions.TripNotFoundException;
import com.github.vinicius2335.planner.utils.creator.TripCreator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Teste Unitário para TripService")
class TripServiceTest {

    @InjectMocks
    private TripService underTest;

    @Mock
    private TripRepository mockTripRepository;

    private TripCreateRequest tripCreateRequest;
    private Trip trip;

    private static final String NOT_FOUND_MESSAGE = "Não foi encontrada nenhuma viagem com o ID:";

    @BeforeEach
    void setUp() {
        tripCreateRequest = TripCreator.mockTripCreateRequest();
        trip = new Trip(tripCreateRequest);
        trip.setId(UUID.randomUUID());

        when(mockTripRepository.save(ArgumentMatchers.any(Trip.class)))
                .thenReturn(trip);

        when(mockTripRepository.findById(any(UUID.class)))
                .thenReturn(Optional.of(trip));

    }

    @Test
    @DisplayName("Adiciona uma nova viagem")
    void createTrip_returnTripSaved_whenSuccessfully() {
        // when
        Trip actual = underTest.createTrip(tripCreateRequest);
        // then
        verify(mockTripRepository, times(1)).save(trip);

        assertThat(actual)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(trip);
    }

    @Test
    @DisplayName("Procura e retorna uma viagem pelo id")
    void findTripById_returnFound_whenSuccessfully() throws TripNotFoundException {
        // when
        Trip actual = underTest.findById(trip.getId());
        // then
        verify(mockTripRepository, times(1)).findById(trip.getId());

        assertThat(actual)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(trip);
    }

    @Test
    @DisplayName("Procura uma viagem pelo id, lança uma excpetion quando id não for encontrado")
    void findTripById_throwsTripNotFoundException_whenNotFoundById() {
        // config
        when(mockTripRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());
        // when
        assertThatThrownBy(() -> underTest.findById(trip.getId()))
                .isInstanceOf(TripNotFoundException.class)
                .hasMessageContaining(NOT_FOUND_MESSAGE);
        // then
        verify(mockTripRepository, times(1)).findById(trip.getId());
    }

    @Test
    @DisplayName("Atualiza e retorna viagem")
    void updateTrip_returnUpdatedTrip_whenSuccessfully() throws TripNotFoundException {
        // when
        Trip actual = underTest.updateTrip(trip.getId(), tripCreateRequest);
        // then
        verify(mockTripRepository, times(1)).save(trip);

        assertThat(actual)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(trip);
    }

    @Test
    @DisplayName("Atualiza viagem, lança uma exception quando id da viagem não for encontrado")
    void updateTrip_thrownsTripNotFoundException_whenTripNotFoundById() {
        // config
        when(mockTripRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());
        // when
        assertThatThrownBy(() -> underTest.updateTrip(trip.getId(), tripCreateRequest))
                .isInstanceOf(TripNotFoundException.class)
                .hasMessageContaining(NOT_FOUND_MESSAGE);
        // then
        verify(mockTripRepository, never()).save(trip);
    }

    @Test
    @DisplayName("Confirma a viagem")
    void confirmTrip_returnConfirmedTrip_whenSuccessfully() throws TripNotFoundException {
        // when
        Trip actual = underTest.confirmTrip(trip.getId());
        // then
        verify(mockTripRepository, times(1)).save(trip);

        assertThat(actual.isConfirmed())
                .isTrue();
    }

    @Test
    @DisplayName("Confirma a viagem, lança uma exception quando viagem não for encontrado pelo id")
    void confirmTrip_thrownsTripNotFoundException_whenTripNotFoundById() {
        // config
        when(mockTripRepository.findById(any(UUID.class)))
                .thenReturn(Optional.empty());
        // when
        assertThatThrownBy(() -> underTest.confirmTrip(trip.getId()))
                .isInstanceOf(TripNotFoundException.class)
                        .hasMessageContaining(NOT_FOUND_MESSAGE);
        // then
        verify(mockTripRepository, never()).save(trip);

    }
}