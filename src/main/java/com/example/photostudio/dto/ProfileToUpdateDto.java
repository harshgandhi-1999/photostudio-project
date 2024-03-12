package com.example.photostudio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProfileToUpdateDto {

    @NotEmpty(message = "name cannot be empty or null")
    private String name;

    @Email(message = "invalid email")
    private String email;
}
