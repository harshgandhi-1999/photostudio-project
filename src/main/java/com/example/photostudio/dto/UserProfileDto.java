package com.example.photostudio.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserProfileDto {
    private String name;
    private String username;
    private String email;
}
