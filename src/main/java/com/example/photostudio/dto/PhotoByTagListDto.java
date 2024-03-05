package com.example.photostudio.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PhotoByTagListDto {
    private List<PhotoByTagDto> photos;
}
