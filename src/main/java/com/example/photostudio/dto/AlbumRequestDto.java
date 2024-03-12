package com.example.photostudio.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AlbumRequestDto {

    @NotEmpty(message = "albumName cannot be empty or null")
    private String albumName;
}
