package com.example.photostudio.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PhotoUploadDto {
    @NotNull(message = "file cannot be null")
    private MultipartFile file;

    @NotEmpty(message = "tag cannot be empty or null")
    private String tag;
}
