package com.github.vinicius2335.planner.integration;

import com.github.vinicius2335.planner.modules.participant.Participant;
import com.github.vinicius2335.planner.modules.participant.dtos.ParticipantNameRequest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.log4j.Log4j2;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.UUID;

@Log4j2
@DisplayName("Teste de Integração para ParticipantController")
class ParticipantControllerIT extends BaseIT {

    @BeforeEach
    void setUp() {
        configRestAsured("/participants");
    }

    @Test
    @DisplayName("Confirma o participante")
    void confirmParticipant_returnStatus200_whenSuccessfully() {
        Participant participantSaved = addParticipantTest();
        ParticipantNameRequest request = new ParticipantNameRequest("Teste Name");

        log.info(participantSaved);

        RestAssured.given()
                .pathParams("participantId", participantSaved.getId())
                .body(request)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .patch("/{participantId}/confirm")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("confirmed", Matchers.equalTo(true))
                .log().all();
    }

    @Test
    @DisplayName("Lança uma exception quando o participante não for encontrado pelo id")
    void confirmParticipant_returnStatus404_whenParticipantNotFoundById() {
        UUID participantId = UUID.randomUUID();
        ParticipantNameRequest request = new ParticipantNameRequest("Teste Name");

        RestAssured.given()
                .pathParams("participantId", participantId)
                .body(request)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .patch("/{participantId}/confirm")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo(NOT_FOUND_TITLE))
                .log().all();
    }

    @Test
    @DisplayName("Lança uma exception quando o request body possui  campos inválidos")
    void confirmParticipant_returnStatus400_whenRequestBodyHaveInvalidFields() {
        Participant participantSaved = addParticipantTest();
        ParticipantNameRequest invalidReq = new ParticipantNameRequest("");

        RestAssured.given()
                .pathParams("participantId", participantSaved.getId())
                .body(invalidReq)
                .accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .when()
                .patch("/{participantId}/confirm")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", Matchers.equalTo(INVALID_FIELDS_TITLE))
                .log().all();
    }
}