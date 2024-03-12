package com.example.photostudio.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AlbumDto {

    @NotNull(message = "albumId cannot be empty or null")
    private Integer albumId;

    @NotEmpty(message = "albumName cannot be empty or null")
    private String albumName;
}
