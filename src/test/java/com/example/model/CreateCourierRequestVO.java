package com.example.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class CreateCourierRequestVO {
    private String login;
    private String password;
    private String firstName;
}
