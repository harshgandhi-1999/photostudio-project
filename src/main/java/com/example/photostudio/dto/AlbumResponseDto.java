package com.example.photostudio.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AlbumResponseDto {
    private AlbumDto albumDto;
    private ResponseDto responseDto;
}
