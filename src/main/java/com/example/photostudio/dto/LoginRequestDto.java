package com.example.photostudio.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    @NotEmpty(message = "username cannot be null or empty")
    private String username;

    @NotEmpty(message = "password cannot be null or empty")
    @Pattern(regexp = "(^[a-zA-Z0-9]{3,}$)",message = "password must be atleast 3 characters long")
    private String password;
}
