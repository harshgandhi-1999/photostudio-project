package com.example.photostudio.repository;

import com.example.photostudio.entity.Album;
import com.example.photostudio.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Integer> {
    Optional<Album> findByAlbumIdAndUser(Integer albumId, User user);
}
