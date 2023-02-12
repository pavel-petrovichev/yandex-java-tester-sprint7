package com.example;

import com.example.model.courier.*;
import com.example.model.order.*;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.response.ValidatableResponse;
import org.apache.http.params.CoreConnectionPNames;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.Timeout;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class ScooterBaseTest {

    @Rule
    public Timeout globalTimeout = Timeout.seconds(30);

    private RestAssuredConfig config;

    protected Faker ruFaker;
    protected Faker enFaker;

    private Set<CreateCourierRequestVO> createdCouriers;

    @Before
    public void setup() {
        System.out.println("setup()");
        RestAssured.baseURI = "https://qa-scooter.praktikum-services.ru/api/v1";
        RestAssured.port = 443;
        config = RestAssured.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam(CoreConnectionPNames.SO_TIMEOUT, 10_000));

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
            Long id = retrieveCourierId(courier);
            deleteCourier(id);
        }
    }

    protected CreateCourierResponseVO createCourier(CreateCourierRequestVO createCourierRequestVO) {
        return createCourier(createCourierRequestVO, SC_CREATED, CreateCourierResponseVO.class);
    }

    protected <T> T createCourier(
            CreateCourierRequestVO request,
            int expectedStatusCode,
            Class<T> responseClass) {
        System.out.printf("creating courier: %s\n", request);
        ValidatableResponse response = given()
                .config(config)
                .body(request)
                .contentType(JSON)
                .log().method().log().uri().log().body()
                .when()
                .post("/courier")
                .then();
        if (response.extract().statusCode() == SC_CREATED) {
            createdCouriers.add(request);
        }
        return response
                .log().status().log().body()
                .statusCode(expectedStatusCode)
                .extract().body().as(responseClass);
    }

    protected LoginResponseVO loginCourier(CreateCourierRequestVO courier) {
        return loginCourier(courier, SC_OK, LoginResponseVO.class);
    }

    protected <T> T loginCourier(
            CreateCourierRequestVO courier,
            int expectedStatusCode,
            Class<T> responseClass) {
        System.out.printf("logging in as '%s'\n", courier.getLogin());
        return given()
                .config(config)
                .body(LoginRequestVO.of(courier.getLogin(), courier.getPassword()))
                .contentType(JSON)
                .log().method().log().uri().log().body()
                .when()
                .post("/courier/login")
                .then()
                .log().status().log().body()
                .statusCode(expectedStatusCode)
                .extract().body().as(responseClass);
    }

    protected Long retrieveCourierId(CreateCourierRequestVO courier) {
        return loginCourier(courier)
                .getId();
    }

    protected void deleteCourier(Long id) {
        System.out.printf("deleting courier: %s\n", id);
        DeleteCourierResponseVO response = given()
                .config(config)
                .log().method().log().uri().log().body()
                .when()
                .delete("/courier/{id}", id)
                .then()
                .log().status().log().body()
                .statusCode(SC_OK)
                .extract().body().as(DeleteCourierResponseVO.class);

        assertThat(response)
                .isEqualTo(DeleteCourierResponseVO.of(true));
    }

    // default courier has login in English and first name in Russian
    // since that's what I would do if I were a courier
    protected CreateCourierRequestVO givenDefaultCourier() {
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

    protected CreateOrderResponseVO createOrder(CreateOrderRequestVO createOrderRequestVO) {
        return createOrder(createOrderRequestVO, SC_CREATED, CreateOrderResponseVO.class);
    }

    protected <T> T createOrder(
            CreateOrderRequestVO request,
            int expectedStatusCode,
            Class<T> responseClass) {
        System.out.printf("creating order: %s\n", request);
        ValidatableResponse response = given()
                .config(config)
                .body(request)
                .contentType(JSON)
                .log().method().log().uri().log().body()
                .when()
                .post("/orders")
                .then();
        return response
                .log().status().log().body()
                .statusCode(expectedStatusCode)
                .extract().body().as(responseClass);
    }

    protected CreateOrderRequestVO givenDefaultOrder() {
        Name name = ruFaker.name();
        return CreateOrderRequestVO.builder()
                .firstName(name.firstName())
                .lastName(name.lastName())
                .address(ruFaker.address().fullAddress())
                .metroStation(MetroStations.station())
                .phone(ruFaker.phoneNumber().phoneNumber())
                .rentTime(ThreadLocalRandom.current().nextInt(1, 8))
                .deliveryDate(LocalDateTime.now().plusDays(1).toLocalDate().toString())
                .comment(ruFaker.howIMetYourMother().catchPhrase())
                .color(List.of(Color.BLACK))
                .build();
    }

    protected GetOrdersListResponseVO getOrdersList() {
        System.out.printf("getting order list\n");
        GetOrdersListResponseVO response = given()
                .config(config)
                .log().method().log().uri().log().body()
                .when()
                .get("/orders")
                .then()
                .log().status().log().body()
                .statusCode(SC_OK)
                .extract().body().as(GetOrdersListResponseVO.class);

        return response;
    }
}
