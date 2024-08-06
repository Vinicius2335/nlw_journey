package com.github.vinicius2335.planner.integration;

import com.github.vinicius2335.planner.modules.participant.Participant;
import com.github.vinicius2335.planner.modules.participant.dtos.ParticipantEmailRequest;
import com.github.vinicius2335.planner.modules.trip.Trip;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@DisplayName("Teste de Integração para o TripParticipantsController")
class TripParticipantsControllerIT extends BaseIT {

    @BeforeEach
    void setUp() {
        configRestAsured("/trips");
    }

    @Test
    @DisplayName("Convida um participante para a viagem")
    void inviteParticipant_returnStatus201_whenSuccessfully() {
        Trip tripSaved = addTripTest();
        ParticipantEmailRequest request = new ParticipantEmailRequest("teste.participant@email.com");

        RestAssured.given()
                .pathParams("tripId", tripSaved.getId())
                .body(request)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .post("/{tripId}/invite")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("participant_id", Matchers.notNullValue())
                .log().all();
    }

    @Test
    @DisplayName("Lança uma exception quando a viagem não for encontrada pelo id")
    void inviteParticipant_returnStatus404_whenTripNotFoundById() {
        UUID tripId = UUID.randomUUID();
        ParticipantEmailRequest request = new ParticipantEmailRequest("teste.participant@email.com");

        RestAssured.given()
                .pathParams("tripId", tripId)
                .body(request)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .post("/{tripId}/invite")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo(NOT_FOUND_TITLE))
                .log().all();
    }

    @Test
    @DisplayName("Lança uma exception quando o request body possui campos inválidos")
    void inviteParticipant_returnStatus400_whenRequestBodyHaveInvalidFields() {
        Trip tripSaved = addTripTest();
        ParticipantEmailRequest invalidReq = new ParticipantEmailRequest("");

        RestAssured.given()
                .pathParams("tripId", tripSaved.getId())
                .body(invalidReq)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .post("/{tripId}/invite")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", Matchers.equalTo(INVALID_FIELDS_TITLE))
                .log().all();
    }

    @Test
    @DisplayName("Retorna todos os participantes de uma viagem")
    void getAllParticipants_returnStatus200_whenSuccessfully() {
        Trip tripSaved = addTripTest();
        addParticipantTest();

        RestAssured.given()
                .pathParams("tripId", tripSaved.getId())
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .get("/{tripId}/participants")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("participants", Matchers.hasSize(1))
                .log().all();
    }

    @Test
    @DisplayName("Lança uma exception quando a viagem não for encontrada pelo id")
    void getAllParticipants_returnStatus404_whenTripNotFoundById() {
        UUID tripId = UUID.randomUUID();
        addParticipantTest();

        RestAssured.given()
                .pathParams("tripId", tripId)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .get("/{tripId}/participants")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo(NOT_FOUND_TITLE))
                .log().all();
    }
}