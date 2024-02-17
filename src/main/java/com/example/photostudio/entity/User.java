package com.example.photostudio.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
/*
    private List<Album> albums = new ArrayList<>();*/
}
