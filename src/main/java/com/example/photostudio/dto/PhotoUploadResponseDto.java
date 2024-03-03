package com.example.photostudio.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PhotoUploadResponseDto {

    private PhotoDto photo;
    private ResponseDto result;
}
