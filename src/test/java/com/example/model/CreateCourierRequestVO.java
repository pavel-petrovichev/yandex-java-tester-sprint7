package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@AllArgsConstructor(staticName = "of")
public class CreateCourierRequestVO {
    private String login;
    private String password;
    private String firstName;
}
