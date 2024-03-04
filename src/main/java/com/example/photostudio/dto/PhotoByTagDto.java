package com.example.photostudio.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
public class PhotoByTagDto {
    private Integer photoId;
    private String name;
    private String url;
    private String tag;
    private String username;
}
