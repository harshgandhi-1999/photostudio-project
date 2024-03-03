package com.example.photostudio.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PhotoDto {
    private Integer photoId;
    private String name;
    private String url;
    private String tag;
    private AlbumDto album;
}
