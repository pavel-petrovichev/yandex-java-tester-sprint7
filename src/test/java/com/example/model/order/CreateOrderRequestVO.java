package com.example.model.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor(staticName = "of")
public class CreateOrderRequestVO {
    String firstName;
    String lastName;
    String address;
    String metroStation;
    String phone;
    Integer rentTime;
    String deliveryDate;
    String comment;
    List<String> color;
}
