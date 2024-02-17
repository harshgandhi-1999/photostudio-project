package com.example.photostudio.service.impl;

import com.example.photostudio.dto.*;
import com.example.photostudio.entity.User;
import com.example.photostudio.exception.NotAuthorizedException;
import com.example.photostudio.exception.ResourceNotFoundException;
import com.example.photostudio.exception.UserAlreadyExistException;
import com.example.photostudio.mapper.UserMapper;
import com.example.photostudio.repository.UserRepository;
import com.example.photostudio.service.AuthService;
import com.example.photostudio.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserMapper userMapper;

    Logger logger = Logger.getLogger(AuthService.class.getName());

    @Override
    public LoginResponseDto authenticateUser(LoginRequestDto loginRequestDto) {
        // check if user exists or not
        Optional<User> optionalUser = userAlreadyExist(loginRequestDto.getUsername());

        if(optionalUser.isEmpty()){
            throw new ResourceNotFoundException("User","username",loginRequestDto.getUsername());
        }

        User user = optionalUser.get();
        UserProfileDto userDetails = userMapper.userToUserProfileDto(user);

        //check if password match
        if(!Objects.equals(user.getPassword(), loginRequestDto.getPassword())){
            throw new NotAuthorizedException("Password is incorrect");
        }

        // generate token
        String token = jwtUtil.generateToken(userDetails);
        logger.info("Token: " + token);
        return new LoginResponseDto(userDetails,token);
    }


    @Override
    public void createUser(SignupRequestDto signupRequestDto) {
        logger.info("Create user");
        // check if user exists or not
        Optional<User> optionalUser = userAlreadyExist(signupRequestDto.getUsername());

        if(optionalUser.isPresent()){
            throw new UserAlreadyExistException(signupRequestDto.getUsername());
        }

        //create user
        User user = userMapper.signUpRequestDtoToUser(signupRequestDto);

        // save user
        userRepository.save(user);
    }


    private Optional<User> userAlreadyExist(String username){
        return userRepository.findByUsername(username);
    }
}
