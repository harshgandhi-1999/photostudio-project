package com.example.photostudio.service.impl;

import com.example.photostudio.dto.LoginRequestDto;
import com.example.photostudio.dto.LoginResponseDto;
import com.example.photostudio.dto.SignupRequestDto;
import com.example.photostudio.dto.UserProfileDto;
import com.example.photostudio.entity.Album;
import com.example.photostudio.entity.User;
import com.example.photostudio.exception.AuthenticationFailedException;
import com.example.photostudio.exception.UserAlreadyExistException;
import com.example.photostudio.mapper.UserMapper;
import com.example.photostudio.repository.AlbumRepository;
import com.example.photostudio.repository.UserRepository;
import com.example.photostudio.service.AuthService;
import com.example.photostudio.utils.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final Logger logger = Logger.getLogger(AuthService.class.getName());

    @Override
    public LoginResponseDto authenticateUser(LoginRequestDto loginRequestDto) {
        logger.info("AUTHENTICATE USER SERVICE");

        try {

            String username = loginRequestDto.getUsername();
            String password = loginRequestDto.getPassword();

            // using spring boot authentication manager to validate user is present in db, but we can also do it manually
            // create a unauthenticated object
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

            // Authenticate user (this checks the password via UserDetailsService + PasswordEncoder)
            // if it is not authenticated then exception will be thrown
            Authentication authentication = authenticationManager.authenticate(authenticationToken);


            // it returns object returned by UserDetailsService.loadUserByUsername() method i.e. User
            User user = (User) authentication.getPrincipal();

            // If successful, generate JWT token using username
            String token = jwtTokenProvider.generateToken(username, List.of("ROLE_USER"));

            UserProfileDto userProfileDto = UserProfileDto.builder()
                    .name(user.getName())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .build();


            return new LoginResponseDto(userProfileDto, token);
        } catch (Exception exc) {
            // if username or password is incorrect then exception will be raised
            logger.info(exc.getLocalizedMessage());
            throw new AuthenticationFailedException("Username or Password is incorrect");
        }
    }


    @Override
    public void createUser(SignupRequestDto signupRequestDto) {
        logger.info("CREATE USER SERVICE");
        // check if user exists or not
        Optional<User> optionalUser = userRepository.findByUsername(signupRequestDto.getUsername());

        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistException(signupRequestDto.getUsername());
        }

        // encode the password
        String encodedPassword = encoder.encode(signupRequestDto.getPassword());
        //logger.info("Encoded Password: " + encodedPassword);
        User user = User.builder()
                .username(signupRequestDto.getUsername())
                .password(encodedPassword)
                .name(signupRequestDto.getName())
                .email(signupRequestDto.getEmail())
                .build();
        // create default album
        Album album = Album.builder()
                .albumName("default")
                .user(user)
                .build();
        user.setAlbums(List.of(album));

        // save in database
        userRepository.save(user);
    }
}
