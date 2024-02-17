package com.example.photostudio.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SignupRequestDto {
    private String username;
    private String password;
    private String name;
    private String email;
}
