package com.example.photostudio.repository;

import com.example.photostudio.entity.Album;
import com.example.photostudio.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

   Optional<User> findByUsername(String username);
}
