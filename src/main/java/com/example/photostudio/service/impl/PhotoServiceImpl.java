package com.example.photostudio.service.impl;

import com.example.photostudio.dto.*;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    public PhotoUploadResponseDto uploadPhoto(String username, PhotoUploadDto photoUploadDto, Integer albumId) {
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
        CloudImageDto cloudImageDto = cloudinaryService.upload(photoUploadDto.getFile());

        Album album = optionalAlbum.get();

        // Create new Photo
        Photo photo = Photo.builder()
                .tag(photoUploadDto.getTag())
                .name(photoUploadDto.getFile().getOriginalFilename())
                .url(cloudImageDto.getUrl())
                .publicId(cloudImageDto.getPublicId())
                .album(album)
                .build();
        album.getPhotos().add(photo);

        photo = photoRepository.save(photo);

        // generate response
        PhotoDto photoDto = photoMapper.photoToPhotoDto(photo);
        ResponseDto responseDto = new ResponseDto(HttpStatus.CREATED.toString(), "Photo uploaded successfully");

        return new PhotoUploadResponseDto(photoDto, responseDto);
    }

    @Override
    public ResponseDto deletePhoto(String username, Integer photoId) {
        logger.info("DELETE PHOTO");
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User", "username", username);
        }

        Optional<Photo> optionalPhoto = photoRepository.findById(photoId);
        if (optionalPhoto.isEmpty()) {
            throw new ResourceNotFoundException("Photo", "photoId", photoId.toString());
        }

        Photo photo = optionalPhoto.get();
        if (!Objects.equals(photo.getAlbum().getUser().getUsername(), username)) {
            throw new ResourceNotFoundException("Photo", "photoId", photoId.toString());
        }

        // delete photo from cloud storage.
        boolean photoDeleted = cloudinaryService.delete(photo.getPublicId());
        if (!photoDeleted) {
            throw new RuntimeException("Unable to delete photo with photoId: " + photoId);
        }

        Album album = photo.getAlbum();

        // remove photo from album's photo list
        album.getPhotos().remove(photo);

        albumRepository.save(album);

        // generate response
        return new ResponseDto(HttpStatus.OK.toString(), "Photo deleted successfully");
    }

    @Override
    public PhotoByTagListDto getAllPhotosByTag(String tag) {
        logger.info("GET ALL PHOTOS BY TAG");
        if (tag == null || tag.isEmpty()) {
            throw new ResourceNotFoundException("Photo", "tag", null);
        }

        List<Photo> photosFromDb = photoRepository.findAllByTag(tag);

        List<PhotoByTagDto> photos = new ArrayList<>();
        for (Photo photo : photosFromDb) {
            PhotoByTagDto photoByTagDto = PhotoByTagDto.builder()
                    .photoId(photo.getPhotoId())
                    .name(photo.getName())
                    .url(photo.getUrl())
                    .tag(photo.getTag())
                    .username(photo.getAlbum().getUser().getUsername())
                    .build();
            photos.add(photoByTagDto);
        }

        return new PhotoByTagListDto(photos);
    }
}
