package com.example;

import com.example.model.courier.CreateCourierRequestVO;
import com.example.model.courier.ErrorResponseVO;
import com.example.model.courier.LoginResponseVO;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;

public class LoginCourierTest extends ScooterBaseTest {

    CreateCourierRequestVO courier;

    @Before
    public void createCourier() {
        System.out.println("createCourier()");
        courier = givenCourier();
        createCourier(courier);
    }

    @Test
    public void successfulLoginReturnsId() {
        LoginResponseVO response = loginCourier(courier);

        assertThat(response.getId())
                .isGreaterThan(0L);
    }

    @Test
    public void loginIsMandatoryToLogin() {
        ErrorResponseVO response = loginCourier(
                courier.toBuilder()
                        .login(null)
                        .build(),
                SC_BAD_REQUEST,
                ErrorResponseVO.class);

        assertThat(response)
                .isEqualTo(ErrorResponseVO.of(
                        SC_BAD_REQUEST,
                        "Недостаточно данных для входа"));
    }

    @Test
    public void passwordIsMandatoryToLogin() {
        ErrorResponseVO response = loginCourier(
                courier.toBuilder()
                        .password(null)
                        .build(),
                SC_BAD_REQUEST,
                ErrorResponseVO.class);

        assertThat(response)
                .isEqualTo(ErrorResponseVO.of(
                        SC_BAD_REQUEST,
                        "Недостаточно данных для входа"));
    }

    @Test
    public void loginMustBeCorrect() {
        ErrorResponseVO response = loginCourier(
                courier.toBuilder()
                        .login(courier.getLogin() + "!")
                        .build(),
                SC_NOT_FOUND,
                ErrorResponseVO.class);

        assertThat(response)
                .isEqualTo(ErrorResponseVO.of(
                        SC_NOT_FOUND,
                        "Учетная запись не найдена"));
    }

    @Test
    public void passwordMustBeCorrect() {
        ErrorResponseVO response = loginCourier(
                courier.toBuilder()
                        .password(courier.getPassword() + "!")
                        .build(),
                SC_NOT_FOUND,
                ErrorResponseVO.class);

        assertThat(response)
                .isEqualTo(ErrorResponseVO.of(
                        SC_NOT_FOUND,
                        "Учетная запись не найдена"));
    }

    @Test
    public void loginAndPasswordMustBeCorrect() {
        ErrorResponseVO response = loginCourier(
                courier.toBuilder()
                        .login(courier.getLogin() + "!")
                        .password(courier.getPassword() + "!")
                        .build(),
                SC_NOT_FOUND,
                ErrorResponseVO.class);

        assertThat(response)
                .isEqualTo(ErrorResponseVO.of(
                        SC_NOT_FOUND,
                        "Учетная запись не найдена"));
    }
}
