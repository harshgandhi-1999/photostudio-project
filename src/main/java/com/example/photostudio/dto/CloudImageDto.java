package com.example.photostudio.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CloudImageDto {
    private String url;
    private String publicId;
}
