package com.example.model.courier;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(staticName = "of")
public class LoginRequestVO {
    private String login;
    private String password;
}
