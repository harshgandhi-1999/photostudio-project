package com.example.photostudio.mapper;

import com.example.photostudio.dto.SignupRequestDto;
import com.example.photostudio.dto.UserProfileDto;
import com.example.photostudio.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserProfileDto userToUserProfileDto(User user);
    User userProfileDtoToUser(UserProfileDto userProfileDto);

    User signUpRequestDtoToUser(SignupRequestDto signupRequestDto);
}
