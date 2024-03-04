package com.example.photostudio.mapper;

import com.example.photostudio.dto.PhotoDto;
import com.example.photostudio.dto.PhotoListDto;
import com.example.photostudio.entity.Photo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PhotoMapper {
    PhotoDto photoToPhotoDto(Photo photo);
}
