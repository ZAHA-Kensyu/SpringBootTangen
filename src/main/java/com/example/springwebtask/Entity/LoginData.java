package com.example.springwebtask.Entity;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginData {
    @NotEmpty
    String id;

    @NotEmpty
    String passWord;
}
