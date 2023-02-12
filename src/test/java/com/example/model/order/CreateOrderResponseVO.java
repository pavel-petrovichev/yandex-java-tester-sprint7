package com.example.model.order;

import lombok.*;

import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class CreateOrderResponseVO {
    private Long track;
}
