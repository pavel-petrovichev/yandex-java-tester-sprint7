package com.example;

import com.example.model.order.CreateOrderRequestVO;
import com.example.model.order.CreateOrderResponseVO;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.example.model.order.Color.BLACK;
import static com.example.model.order.Color.GRAY;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
@RequiredArgsConstructor
public class OrderCreateTest extends ScooterBaseTest {

    CreateOrderRequestVO order;
    CreateOrderResponseVO response;

    final List<String> colors;

    @Parameterized.Parameters(name = "colors: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {List.of(BLACK)},
                {List.of(GRAY)},
                {List.of(BLACK, GRAY)},
                {List.of()},
        });
    }

    @Test
    public void createOrder() {
        order = givenDefaultOrder();

        response = createOrder(order);

        assertThat(response.getTrack())
                .isGreaterThan(0L);
    }
}
