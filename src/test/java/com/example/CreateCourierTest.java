package com.example;

import com.example.model.CreateCourierErrorVO;
import com.example.model.CreateCourierRequestVO;
import com.example.model.CreateCourierResponseVO;
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

        CreateCourierErrorVO secondAttempt = createCourier(
                courier,
                SC_CONFLICT,
                CreateCourierErrorVO.class);
        assertThat(secondAttempt)
                .isEqualTo(CreateCourierErrorVO.of(
                        SC_CONFLICT,
                        "Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    public void loginIsMandatory() {
        courier = givenCourier();
        courier.setLogin(null);

        CreateCourierErrorVO response = createCourier(
                courier,
                SC_BAD_REQUEST,
                CreateCourierErrorVO.class);
        assertThat(response)
                .isEqualTo(CreateCourierErrorVO.of(
                        SC_BAD_REQUEST,
                        "Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void passwordIsMandatory() {
        courier = givenCourier();
        courier.setPassword(null);

        CreateCourierErrorVO response = createCourier(
                courier,
                SC_BAD_REQUEST,
                CreateCourierErrorVO.class);
        assertThat(response)
                .isEqualTo(CreateCourierErrorVO.of(
                        SC_BAD_REQUEST,
                        "Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void firstNameIsMandatory() {
        courier = givenCourier();
        courier.setFirstName(null);

        CreateCourierErrorVO response = createCourier(
                courier,
                SC_BAD_REQUEST,
                CreateCourierErrorVO.class);
        assertThat(response)
                .isEqualTo(CreateCourierErrorVO.of(
                        SC_BAD_REQUEST,
                        "Недостаточно данных для создания учетной записи"));
    }
}
