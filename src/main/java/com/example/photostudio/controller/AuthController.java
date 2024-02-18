package com.example.photostudio.controller;

import com.example.photostudio.dto.LoginRequestDto;
import com.example.photostudio.dto.LoginResponseDto;
import com.example.photostudio.dto.ResponseDto;
import com.example.photostudio.dto.SignupRequestDto;
import com.example.photostudio.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto userData) {
        LoginResponseDto loginResponseDto = authService.authenticateUser(userData);

        return ResponseEntity.status(HttpStatus.OK).body(loginResponseDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseDto> signup(@RequestBody SignupRequestDto userData) {
        authService.createUser(userData);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(HttpStatus.CREATED.toString(), "Signup successful"));
    }
}
