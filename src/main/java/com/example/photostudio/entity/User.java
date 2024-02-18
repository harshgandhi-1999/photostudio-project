package com.example.photostudio.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Column(name = "username",unique = true,nullable = false)
    private String username;

    private String password;

    private String name;

    private String email;

    @OneToMany(cascade = {CascadeType.MERGE,CascadeType.REFRESH,CascadeType.DETACH,CascadeType.REMOVE},fetch = FetchType.EAGER,mappedBy = "userId")
    @Column(name = "albums")
    private List<Album> albums = new ArrayList<>();
}
