package com.example;

import com.example.model.*;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class ScooterBaseTest {

    protected Faker ruFaker;
    protected Faker enFaker;

    protected Set<CreateCourierRequestVO> createdCouriers;

    @Before
    public void setup() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/api/v1/courier";
        RestAssured.port = 443;

        ruFaker = new Faker(Locale.forLanguageTag("RU"));
        enFaker = new Faker(Locale.ENGLISH);

        createdCouriers = new HashSet<>();
    }

    @After
    public void deleteCreatedCouriers() {
        if (createdCouriers.isEmpty()) {
            System.out.println("no couriers were not created, no clean up required");
        }
        for (CreateCourierRequestVO courier : createdCouriers) {
            Long id = retrieveCourierId(courier.getLogin(), courier.getPassword());
            deleteCourier(id);
        }
    }

    protected CreateCourierResponseVO createCourier(CreateCourierRequestVO createCourierRequestVO) {
        return createCourier(createCourierRequestVO, SC_CREATED, CreateCourierResponseVO.class);
    }

    protected <T> T createCourier(
            CreateCourierRequestVO createCourierRequestVO,
            int expectedStatusCode,
            Class<T> clazz) {
        System.out.printf("creating courier: %s\n", createCourierRequestVO);
        ValidatableResponse response = given()
                .body(createCourierRequestVO)
                .contentType(JSON)
                .log().method().log().uri().log().body()
                .when()
                .post()
                .then();
        if (response.extract().statusCode() == SC_CREATED) {
            createdCouriers.add(createCourierRequestVO);
        }
        return response
                .log().status().log().body()
                .statusCode(expectedStatusCode)
                .extract().body().as(clazz);
    }

    protected Long retrieveCourierId(String login, String password) {
        System.out.printf("logging in as '%s'\n", login);
        LoginRequestVO loginRequestVO = LoginRequestVO.of(login, password);
        LoginResponseVO loginResponseVO = given()
                .body(loginRequestVO)
                .contentType(JSON)
                .log().method().log().uri().log().body()
                .when()
                .post("/login")
                .then()
                .log().status().log().body()
                .statusCode(SC_OK)
                .extract().body().as(LoginResponseVO.class);
        return loginResponseVO.getId();
    }

    protected void deleteCourier(Long id) {
        System.out.printf("deleting courier: %s\n", id);
        DeleteCourierResponseVO response = given()
                .log().method().log().uri().log().body()
                .when()
                .delete("/{id}", id)
                .then()
                .log().status().log().body()
                .statusCode(SC_OK)
                .extract().body().as(DeleteCourierResponseVO.class);

        assertThat(response)
                .isEqualTo(DeleteCourierResponseVO.of(true));
    }

    // default courier has login in English and first name in Russian
    // since that's what I would do if I were a courier
    protected CreateCourierRequestVO givenCourier() {
        return CreateCourierRequestVO.of(
                generateLogin(),
                generatePassword(),
                generateFirstName());
    }

    private String generateLogin() {
        return enFaker.name().username();
    }

    private String generatePassword() {
        return enFaker.internet().password(10, 20, true, true, true);
    }

    private String generateFirstName() {
        return ruFaker.name().firstName();
    }
}
