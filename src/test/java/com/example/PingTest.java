package com.example;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class PingTest {

    @Before
    public void setup() {
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/api/v1/ping";
        RestAssured.port = 443;
    }

    @Test
    public void ping() {
        RestAssured.given().log().all()
                .get()
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_OK)
                .body(equalTo("pong;"));
    }
}
