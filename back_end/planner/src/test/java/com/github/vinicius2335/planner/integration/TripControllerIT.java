package com.github.vinicius2335.planner.integration;

import com.github.vinicius2335.planner.modules.trip.Trip;
import com.github.vinicius2335.planner.modules.trip.dtos.TripCreateRequest;
import com.github.vinicius2335.planner.utils.creator.TripCreator;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@DisplayName("Teste de Integração para o TripController")
class TripControllerIT extends BaseIT {

    @BeforeEach
    void setUp() {
        configRestAsured("/trips");
    }

    @Test
    @DisplayName("Cria uma nova viagem")
    void createTrip_returnStatus201_whenSuccessfully() {
        TripCreateRequest request = TripCreator.mockTripCreateRequest();

        given()
                .body(request)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .log().all();
    }

    @Test
    @DisplayName("Lança uma exception quando existir campos incorretos no request body")
    void createTrip_returnStatus400_whenRequestBodyHaveInvalidFields() {
        TripCreateRequest invalidReq = TripCreator.mockInvalidTripCreateRequest();

        given()
                .body(invalidReq)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post()
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", Matchers.equalTo(INVALID_FIELDS_TITLE))
                .log().all();
    }

    @Test
    @DisplayName("Recupera os detalhes da viagem")
    void getTripDetails_returnStatus200_whenSuccessfully() {
        Trip tripSaved = addTripTest();

        given()
                .pathParams("tripId", tripSaved.getId())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("/{tripId}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("destination", Matchers.equalTo(tripSaved.getDestination()))
                .body("owner_name", Matchers.equalTo(tripSaved.getOwnerName()))
                .body("owner_email", Matchers.equalTo(tripSaved.getOwnerEmail()))
                .log().all();
    }

    @Test
    @DisplayName("Lança uma exception quando viagem não for encontrado pelo id")
    void getTripDetails_returnStatus404_whenTripNotFoundById() {
        UUID tripId = UUID.randomUUID();

        given()
                .pathParams("tripId", tripId)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("/{tripId}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo(NOT_FOUND_TITLE))
                .log().all();
    }

    @Test
    @DisplayName("Atualiza uma viagem")
    void updateTrip_returnStatus200_whenSuccessfully() {
        Trip tripSaved = addTripTest();
        TripCreateRequest updateReq = TripCreator.mockUpdateTripCreateRequest();

        given()
                .pathParams("tripId", tripSaved.getId())
                .body(updateReq)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .put("/{tripId}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("destination", Matchers.equalTo(updateReq.destination()))
                .log().all();
    }

    @Test
    @DisplayName("Lança uma exception quando a viagem não for encontrado")
    void updateTrip_returnStatus404_whenTripNotFoundById() {
        UUID tripId = UUID.randomUUID();
        TripCreateRequest updateReq = TripCreator.mockUpdateTripCreateRequest();

        given()
                .pathParams("tripId", tripId)
                .body(updateReq)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .put("/{tripId}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo(NOT_FOUND_TITLE))
                .log().all();
    }

    @Test
    @DisplayName("Lança uma exception quando existir campos inválidos no request body")
    void updateTrip_returnStatus400_whenRequestBodyHaveInvalidFields() {
        UUID tripId = UUID.randomUUID();
        TripCreateRequest invalidReq = TripCreator.mockInvalidTripCreateRequest();

        given()
                .pathParams("tripId", tripId)
                .body(invalidReq)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .put("/{tripId}")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", Matchers.equalTo(INVALID_FIELDS_TITLE))
                .log().all();
    }

    @Test
    @DisplayName("Confirma a viagem")
    void confirmTrip_returnStatus200_whenSuccessfully() {
        Trip tripSaved = addTripTest();

        given()
                .pathParams("tripId", tripSaved.getId())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("/{tripId}/confirm")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("confirmed", Matchers.equalTo(true))
                .log().all();
    }

    @Test
    @DisplayName("Lança uma exception quando viagem não for encontrada pelo id")
    void confirmTrip_returnStatus404_whenTripNotFoundById() {
        UUID tripId = UUID.randomUUID();

        given()
                .pathParams("tripId", tripId)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("/{tripId}/confirm")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo(NOT_FOUND_TITLE))
                .log().all();
    }
}