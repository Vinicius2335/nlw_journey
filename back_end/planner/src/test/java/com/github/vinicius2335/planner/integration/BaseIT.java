package com.github.vinicius2335.planner.integration;

import com.github.vinicius2335.planner.modules.activity.Activity;
import com.github.vinicius2335.planner.modules.activity.ActivityRepository;
import com.github.vinicius2335.planner.modules.link.Link;
import com.github.vinicius2335.planner.modules.link.LinkRepository;
import com.github.vinicius2335.planner.modules.participant.Participant;
import com.github.vinicius2335.planner.modules.participant.ParticipantRepository;
import com.github.vinicius2335.planner.modules.trip.Trip;
import com.github.vinicius2335.planner.modules.trip.TripRepository;
import com.github.vinicius2335.planner.utils.creator.ActivityCreator;
import com.github.vinicius2335.planner.utils.creator.LinkCreator;
import com.github.vinicius2335.planner.utils.creator.ParticipantCreator;
import com.github.vinicius2335.planner.utils.creator.TripCreator;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@AutoConfigureTestDatabase
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = {"classpath:application-test.properties"})
@ActiveProfiles("test")
public abstract class BaseIT {
    @LocalServerPort
    protected int port;

    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private ParticipantRepository participantRepository;
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private ActivityRepository activityRepository;

    protected static Trip trip;
    protected static Participant participant;
    protected static Link link;
    protected static Activity activity;

    protected static final String NOT_FOUND_TITLE = "Entidade não encontrada";
    protected static final String INVALID_FIELDS_TITLE = "Um ou mais campos estão inválidos";

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration
                    .aConfig().withUser("teste@email.com", "password"));

    @BeforeAll
    static void beforeAll() {
        trip = TripCreator.mockTrip();
        participant = ParticipantCreator.mockParticipant(trip);
        link = LinkCreator.mockLink(trip);
        activity = ActivityCreator.mockActivity(trip);
    }

    protected void configRestAsured(String endpoint){
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost:" + port + endpoint;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    protected String localUrl(String endpoint) {
        return "http://localhost:" + port + endpoint;
    }

    protected Trip addTripTest(){
        return tripRepository.save(trip);
    }

    protected Participant addParticipantTest(){
        Trip tripSaved = addTripTest();
        participant.setTrip(tripSaved);
        return participantRepository.save(participant);
    }

    protected Activity addActivityTest(){
        Trip tripSaved = addTripTest();
        activity.setTrip(tripSaved);
        return activityRepository.save(activity);
    }

    protected Link addLinkTest(){
        Trip tripSaved = addTripTest();
        link.setTrip(tripSaved);
        return linkRepository.save(link);
    }
}
