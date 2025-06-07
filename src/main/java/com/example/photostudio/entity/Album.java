package com.example.photostudio.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id")
    private Integer albumId;

    @Column(name = "album_name")
    private String albumName;

    // owning side is album as album is having foreign key so here we will use JoinColumn annotation
    // Dont mention referencedColumnName attribute in JoinColumn as It defaults to referencing the primary key (@Id) column of the target entity
    // default fetch type is eager for ManyToOne
    @ManyToOne
    @JoinColumn(name = "user_id")  // user_id is the name of the foreign key in this album table
    @JsonIgnore
    private User user;

    // we can also remove obove field user, if we dont need bidirectional relationship
    // i.e. we dont need User inside album.
    // then JoinColumn annotation will be used in User entity above List<Album> albums;
    // In a unidirectional @OneToMany relationship with @JoinColumn, JPA will automatically create a foreign key column in the Album table.

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "album", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Photo> photos;

    public void removePhoto(Photo photo){
        photos.remove(photo);
        photo.setAlbum(null);
    }
}
