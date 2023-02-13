package com.example.model.order;

import lombok.*;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class GetOrdersListResponseVO {
    private List<OrderResponseVO> orders;
    private PageInfo pageInfo;
    private List<AvailableStation> availableStations;
}
