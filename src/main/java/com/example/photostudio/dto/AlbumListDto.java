package com.example.photostudio.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AlbumListDto {
    private List<AlbumDto> albums;
}
