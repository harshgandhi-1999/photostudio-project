package com.example.photostudio.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDto {
    private UserProfileDto userDetails;
    private String token;
}
