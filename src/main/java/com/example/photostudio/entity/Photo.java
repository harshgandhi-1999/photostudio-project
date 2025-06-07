package com.example.photostudio.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer photoId;

    private String name;

    private String url;

    private String publicId;

    private String tag;

    @ManyToOne
    @JoinColumn(name = "album_id")
    @JsonIgnore
    private Album album;
}
