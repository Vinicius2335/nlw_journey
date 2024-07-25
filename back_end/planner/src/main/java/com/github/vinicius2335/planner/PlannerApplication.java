package com.github.vinicius2335.planner;

import com.github.javafaker.Faker;
import com.github.vinicius2335.planner.modules.activity.Activity;
import com.github.vinicius2335.planner.modules.activity.ActivityRepository;
import com.github.vinicius2335.planner.modules.link.Link;
import com.github.vinicius2335.planner.modules.link.LinkRepository;
import com.github.vinicius2335.planner.modules.participant.Participant;
import com.github.vinicius2335.planner.modules.participant.ParticipantRepository;
import com.github.vinicius2335.planner.modules.trip.Trip;
import com.github.vinicius2335.planner.modules.trip.TripRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@Log4j2
public class PlannerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlannerApplication.class, args);
    }

	// Preenche o banco automaticamente para testes
    @Bean
    CommandLineRunner seedDatabase(
            TripRepository tripRepository,
            ParticipantRepository participantRepository,
            LinkRepository linkRepository,
            ActivityRepository activityRepository
    ) {
        Faker faker = new Faker(new Locale("pt-BR"));

		return args -> {
			Trip trip = Trip.builder()
					.destination(faker.address().city())
					.ownerName(faker.name().fullName())
					.ownerEmail(faker.internet().emailAddress())
					.startsAt(LocalDateTime.now())
					.endsAt(LocalDateTime.now().plusDays(10))
					.isConfirmed(false)
					.build();

			Trip tripSaved = tripRepository.save(trip);

			for (int i = 0; i < 3; i++) {
				Activity activity = Activity.builder()
						.title(faker.esports().game())
						.occursAt(faker.date()
								.future(1, TimeUnit.DAYS).toInstant()
								.atZone(ZoneId.systemDefault())
								.toLocalDateTime())
						.trip(tripSaved)
						.build();

				activityRepository.save(activity);
			}

			Link link = Link.builder()
					.title("Google")
					.url("www.google.com.br")
					.trip(tripSaved)
					.build();

			linkRepository.save(link);

			Participant participant = Participant
					.builder()
					.name(faker.name().fullName())
					.email(faker.internet().emailAddress())
					.trip(tripSaved)
					.isConfirmed(true)
					.build();

			participantRepository.save(participant);

			log.info("Seed Data Successfully!");
			log.info("Trip id => {}", tripSaved.getId());
		};
	}

}
