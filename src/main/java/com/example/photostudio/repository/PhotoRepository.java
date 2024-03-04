package com.example.photostudio.repository;

import com.example.photostudio.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo,Integer> {
    List<Photo> findAllByTag(String tag);
}
