package com.github.vinicius2335.planner.integration;

import com.github.vinicius2335.planner.modules.link.Link;
import com.github.vinicius2335.planner.modules.link.dtos.LinkCreateRequest;
import com.github.vinicius2335.planner.modules.trip.Trip;
import com.github.vinicius2335.planner.utils.creator.LinkCreator;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@DisplayName("Teste de Integração para TripLinksController")
class TripLinksControllerTest extends BaseIT {

    @BeforeEach
    void setUp() {
        configRestAsured("/trips");
    }

    @Test
    @DisplayName("Registra um novo link para a viagem")
    void registerLink_returnStatus201_whenSuccessfully() {

        Trip tripSaved = addTripTest();
        LinkCreateRequest request = LinkCreator.mockLinkCreateRequest(link.getTitle(), link.getUrl());

        given()
                .pathParams("tripId", tripSaved.getId())
                .body(request)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/{tripId}/links")
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all();
    }

    @Test
    @DisplayName("Lança uma exception quando tiver campos inválidos no corpo da requisição")
    void registerLink_returnStatus400_whenRequestBodyHaveInvalidFields(){

        Trip tripSaved = addTripTest();
        LinkCreateRequest invalidRequest = LinkCreator.mockInvalidLinkCreateRequest();

        given()
                .pathParams("tripId", tripSaved.getId())
                .body(invalidRequest)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/{tripId}/links")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("title", Matchers.equalTo(INVALID_FIELDS_TITLE))
                .log().all();
    }

    @Test
    @DisplayName("Lança uma exception quando à viagem não for encontrada pelo id")
    void registerLink_returnStatus404_whenTripNotFoundById(){
        UUID tripId = UUID.randomUUID();
        LinkCreateRequest request = LinkCreator.mockLinkCreateRequest(link.getTitle(), link.getUrl());

        given()
                .pathParams("tripId",tripId)
                .body(request)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .post("/{tripId}/links")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo(NOT_FOUND_TITLE))
                .log().all();
    }

    @Test
    @DisplayName("Retorna uma lista de links relacionado a uma viagem")
    void getAllLinks_returnStatus200_whenSuccessfully() {
        Link linkSaved = addLinkTest();

        given()
                .pathParams("tripId", linkSaved.getTrip().getId())
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("/{tripId}/links")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("links", Matchers.hasSize(1))
                .body("links[0].id", Matchers.equalTo(linkSaved.getId().toString()))
                .body("links[0].title", Matchers.equalTo(linkSaved.getTitle()))
                .body("links[0].url", Matchers.equalTo(linkSaved.getUrl()))
                .log().all();
    }

    @Test
    @DisplayName("Not Found quando id da viagem não for encontrado")
    void getAllLinks_returnStatus404_whenTripNotFoundById() {
        UUID tripId = UUID.randomUUID();

        given()
                .pathParams("tripId", tripId)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .when()
                .get("/{tripId}/links")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("title", Matchers.equalTo(NOT_FOUND_TITLE))
                .log().all();
    }
}