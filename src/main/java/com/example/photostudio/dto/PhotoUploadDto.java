package com.example.photostudio.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PhotoUploadDto {
    private MultipartFile file;
    private String tag;
}
