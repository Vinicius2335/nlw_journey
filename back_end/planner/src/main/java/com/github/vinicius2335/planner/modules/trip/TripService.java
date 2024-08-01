package com.github.vinicius2335.planner.modules.trip;

import com.github.vinicius2335.planner.modules.trip.dtos.TripCreateRequest;
import com.github.vinicius2335.planner.modules.trip.exceptions.TripNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TripService {
    private final TripRepository tripRepository;

    /**
     * Registra uma nova viagem
     * @param request objeto que apresenta os campos necessário para criar uma nova viagem
     * @return {@code Trip} - viagem registrada
     */
    public Trip createTrip(TripCreateRequest request){
        Trip newTrip = new Trip(request);

        return tripRepository.save(newTrip);
    }

    /**
     * Procura uma viagem pelo id, se não for encontrado, lança uma exception.
     * @param tripId identificador da viagem
     * @return {@code Trip} - viagem encontrada
     * @throws TripNotFoundException quando viagem nao for encontrado pelo {@code tripId}
     */
    public Trip findById(UUID tripId) throws TripNotFoundException {
        return tripRepository.findById(tripId)
                .orElseThrow(
                        () -> new TripNotFoundException("Não foi encontrada nenhuma viagem com o ID: " + tripId)
                );
    }

    /**
     * Atualiza uma viagem, se id não for encontrado, lança uma exception.
     * @param tripId identificador da viagem
     * @param request objeto representa uma viagem atualizada
     * @return {@code Trip} - viagem atualizada
     * @throws TripNotFoundException quando viagem nao for encontrado pelo {@code tripId}
     */
    public Trip updateTrip(UUID tripId, TripCreateRequest request)
            throws TripNotFoundException {
        Trip trip = findById(tripId);

        trip.updateTrip(request);

        return tripRepository.save(trip);
    }

    /**
     * Confirma a viagem
     * @param tripId identificador da viagem
     * @return {@code Trip} atualizado
     * @throws TripNotFoundException quando à viagem não for encontrado pelo {@code tripId}
     */
    public Trip confirmTrip(UUID tripId) throws TripNotFoundException {
        Trip trip = findById(tripId);

        trip.setConfirmed(true);
        return tripRepository.save(trip);
    }
}
