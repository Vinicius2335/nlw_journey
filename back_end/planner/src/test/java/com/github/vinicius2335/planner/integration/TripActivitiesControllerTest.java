package com.github.vinicius2335.planner.integration;

import com.github.vinicius2335.planner.modules.activity.Activity;
import com.github.vinicius2335.planner.modules.activity.dtos.ActivityCreateRequest;
import com.github.vinicius2335.planner.modules.trip.Trip;
import com.github.vinicius2335.planner.utils.creator.ActivityCreator;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@DisplayName("Teste de Integração para TripActivitiesController")
class TripActivitiesControllerTest extends BaseIT{

    @Value("${mensagem.test}")
    private String teste;

    @BeforeEach
    void setUp() {
        configRestAsured("/trips");
    }

    @Test
    @DisplayName("Cadastra uma atividade e retorna o id da atividade criada")
    void registerActivity_returnStatus200_whenSuccessfully() {
        Trip tripSaved = addTripTest();
        ActivityCreateRequest request = ActivityCreator
                .mockActivityCreateRequest(activity.getTitle(), activity.getOccursAt().toString());

        given()
                .pathParams("tripId", tripSaved.getId())
                .body(request)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/{tripId}/activities")
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all();
    }

    @Test
    @DisplayName("Lança uma exception quando request body possui campos inválidos")
    void registerActivity_returnStatus400_whenRequestBodyHaveInvalidFields() {
        Trip tripSaved = addTripTest();
        ActivityCreateRequest invalidRequest = ActivityCreator.mockInvalidActivityCreateRequest();

        given()
                .pathParams("tripId", tripSaved.getId())
                .body(invalidRequest)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/{tripId}/activities")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", Matchers.equalTo(INVALID_FIELDS_TITLE))
                .log().all();
    }

    @Test
    @DisplayName("Lança uma exception quando Viagem não for encontrado pelo id")
    void registerActivity_returnStatus404_whenTripNotFoundById() {
        UUID tripId = UUID.randomUUID();
        ActivityCreateRequest request = ActivityCreator
                .mockActivityCreateRequest(activity.getTitle(), activity.getOccursAt().toString());

        given()
                .pathParams("tripId", tripId)
                .body(request)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/{tripId}/activities")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo(NOT_FOUND_TITLE))
                .log().all();
    }

    @Test
    @DisplayName("Retornar uma lista de atividades relacionadas a uma viagem")
    void getAllActivities_returnStatus200_whenSuccessfully() {
        Activity activitySaved = addActivityTest();

        given()
                .pathParams("tripId", activitySaved.getTrip().getId())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("/{tripId}/activities")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("activities", Matchers.hasSize(1))
                .body("activities[0].activities[0].id", Matchers.equalTo(activitySaved.getId().toString()))
                .log().all();
    }

    @Test
    @DisplayName("Lança uma exception quando id da viagem não for encontrado")
    void getAllActivities_returnStatus404_whenTripIdNotFound() {
        UUID tripId = UUID.randomUUID();

        given()
                .pathParams("tripId", tripId)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("/{tripId}/activities")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .log().all();
    }

    @Test
    @DisplayName("Testando se application-test.properties está sendo usado")
    void application_properties(){
        Assertions.assertThat(teste).isEqualTo("Test");
    }
}