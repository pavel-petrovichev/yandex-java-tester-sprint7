package com.example.model.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor(staticName = "of")
public class GetOrdersListRequestVO {
}
