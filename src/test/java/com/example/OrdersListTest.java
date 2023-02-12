package com.example;

import com.example.model.order.GetOrdersListRequestVO;
import com.example.model.order.GetOrdersListResponseVO;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OrdersListTest extends ScooterBaseTest {

    GetOrdersListRequestVO request;
    GetOrdersListResponseVO response;

    @Test
    public void retrieveOrderList() {
        request = GetOrdersListRequestVO.of();

        response = getOrdersList();

        assertThat(response.getOrders())
                .isNotEmpty();
        assertThat(response.getOrders().stream().findFirst().get().getId())
                .isGreaterThan(0L);
    }
}
