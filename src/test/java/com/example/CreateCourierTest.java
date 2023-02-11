package com.example;

import com.example.model.CreateCourierRequestVO;
import com.example.model.CreateCourierResponseVO;
import com.example.model.ErrorResponseVO;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_CONFLICT;
import static org.assertj.core.api.Assertions.assertThat;

public class CreateCourierTest extends ScooterBaseTest {

    CreateCourierRequestVO courier;

    @Test
    public void createCourier() {
        courier = givenCourier();

        CreateCourierResponseVO response = createCourier(courier);

        assertThat(response)
                .isEqualTo(CreateCourierResponseVO.of(true));
    }

    @Test
    public void loginCanBeCyrillic() {
        courier = CreateCourierRequestVO.of(
                ruFaker.name().username(),
                ruFaker.internet().password(10, 20, true, true, true),
                ruFaker.name().firstName());

        CreateCourierResponseVO response = createCourier(courier);

        assertThat(response)
                .isEqualTo(CreateCourierResponseVO.of(true));
    }


    @Test
    public void firstNameCanBeEnglish() {
        courier = CreateCourierRequestVO.of(
                ruFaker.name().username(),
                ruFaker.internet().password(10, 20, true, true, true),
                enFaker.name().firstName());

        CreateCourierResponseVO response = createCourier(courier);

        assertThat(response)
                .isEqualTo(CreateCourierResponseVO.of(true));
    }

    @Test
    public void impossibleToCreateSameCourierTwice() {
        courier = givenCourier();

        CreateCourierResponseVO response = createCourier(courier);
        assertThat(response)
                .isEqualTo(CreateCourierResponseVO.of(true));

        ErrorResponseVO secondAttempt = createCourier(
                courier,
                SC_CONFLICT,
                ErrorResponseVO.class);
        assertThat(secondAttempt)
                .isEqualTo(ErrorResponseVO.of(
                        SC_CONFLICT,
                        "Этот логин уже используется"));
    }

    @Test
    public void loginIsMandatory() {
        courier = givenCourier();

        ErrorResponseVO response = createCourier(
                courier.toBuilder()
                        .login(null)
                        .build(),
                SC_BAD_REQUEST,
                ErrorResponseVO.class);
        assertThat(response)
                .isEqualTo(ErrorResponseVO.of(
                        SC_BAD_REQUEST,
                        "Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void passwordIsMandatory() {
        courier = givenCourier();

        ErrorResponseVO response = createCourier(
                courier.toBuilder()
                        .password(null)
                        .build(),
                SC_BAD_REQUEST,
                ErrorResponseVO.class);
        assertThat(response)
                .isEqualTo(ErrorResponseVO.of(
                        SC_BAD_REQUEST,
                        "Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void firstNameIsMandatory() {
        courier = givenCourier();

        ErrorResponseVO response = createCourier(
                courier.toBuilder()
                        .firstName(null)
                        .build(),
                SC_BAD_REQUEST,
                ErrorResponseVO.class);
        assertThat(response)
                .isEqualTo(ErrorResponseVO.of(
                        SC_BAD_REQUEST,
                        "Недостаточно данных для создания учетной записи"));
    }
}
