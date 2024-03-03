package com.example.photostudio.service.impl;

import com.example.photostudio.dto.PhotoDto;
import com.example.photostudio.dto.PhotoUploadDto;
import com.example.photostudio.dto.PhotoUploadResponseDto;
import com.example.photostudio.dto.ResponseDto;
import com.example.photostudio.entity.Album;
import com.example.photostudio.entity.Photo;
import com.example.photostudio.entity.User;
import com.example.photostudio.exception.CannotPerformOperationException;
import com.example.photostudio.exception.ResourceNotFoundException;
import com.example.photostudio.mapper.PhotoMapper;
import com.example.photostudio.repository.AlbumRepository;
import com.example.photostudio.repository.PhotoRepository;
import com.example.photostudio.repository.UserRepository;
import com.example.photostudio.service.CloudinaryService;
import com.example.photostudio.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class PhotoServiceImpl implements PhotoService {

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private PhotoRepository photoRepository;

    @Autowired
    private PhotoMapper photoMapper;

    private final Logger logger = Logger.getLogger(PhotoService.class.getName());

    @Override
    public PhotoUploadResponseDto uploadPhoto(PhotoUploadDto photoUploadDto, Integer albumId, String username) {
        if (photoUploadDto.getFile().isEmpty()) {
            throw new CannotPerformOperationException("UPLOAD", "photo", "file", null);
        }

        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User", "username", username);
        }
        User user = optionalUser.get();
        Optional<Album> optionalAlbum = albumRepository.findByAlbumIdAndUser(albumId, user);
        if (optionalAlbum.isEmpty()) {
            throw new ResourceNotFoundException("album", "albumId", albumId.toString());
        }

        // upload photo to cloud storage
        String url = cloudinaryService.upload(photoUploadDto.getFile());

        Album album = optionalAlbum.get();

        // Create new Photo
        Photo photo = Photo.builder()
                .tag(photoUploadDto.getTag())
                .url(url)
                .album(album)
                .build();
        album.getPhotos().add(photo);

        photo = photoRepository.save(photo);

        // generate response
        PhotoDto photoDto = photoMapper.photoToPhotoDto(photo);
        ResponseDto responseDto = new ResponseDto(HttpStatus.CREATED.toString(), "Photo uploaded successfully");

        return new PhotoUploadResponseDto(photoDto, responseDto);
    }
}
