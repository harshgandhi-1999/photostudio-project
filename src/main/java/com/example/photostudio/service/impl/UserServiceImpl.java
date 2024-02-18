package com.example.photostudio.service.impl;

import com.example.photostudio.dto.UserProfileDto;
import com.example.photostudio.entity.User;
import com.example.photostudio.exception.ResourceNotFoundException;
import com.example.photostudio.mapper.UserMapper;
import com.example.photostudio.repository.UserRepository;
import com.example.photostudio.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserProfileDto getUserByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User", "username", username);
        }
        return userMapper.userToUserProfileDto(optionalUser.get());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(optionalUser.get().getUsername())
                .password(optionalUser.get().getPassword())
                .build();
    }
}
