package com.example.photostudio.service;


import com.example.photostudio.dto.LoginRequestDto;
import com.example.photostudio.dto.LoginResponseDto;
import com.example.photostudio.dto.SignupRequestDto;

public interface AuthService {
    LoginResponseDto authenticateUser(LoginRequestDto loginRequestDto);

    void createUser(SignupRequestDto signupRequestDto);
}
