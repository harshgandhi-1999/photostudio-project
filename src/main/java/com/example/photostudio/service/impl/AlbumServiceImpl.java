package com.example.photostudio.service.impl;

import com.example.photostudio.dto.*;
import com.example.photostudio.entity.Album;
import com.example.photostudio.entity.User;
import com.example.photostudio.exception.CannotPerformOperationException;
import com.example.photostudio.exception.ResourceNotFoundException;
import com.example.photostudio.mapper.AlbumMapper;
import com.example.photostudio.repository.AlbumRepository;
import com.example.photostudio.repository.UserRepository;
import com.example.photostudio.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class AlbumServiceImpl implements AlbumService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private AlbumMapper albumMapper;

    private final Logger logger = Logger.getLogger(AlbumService.class.getName());

    @Override
    public AlbumResponseDto createNewAlbum(AlbumRequestDto albumRequestDto, String username) {
        logger.info("CREATE NEW ALBUM");
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User", "username", username);
        }
        User user = optionalUser.get();
        Album album = Album.builder()
                .albumName(albumRequestDto.getAlbumName())
                .user(user)
                .build();

        user.getAlbums().add(album);

        // save new album in db
        album = albumRepository.save(album);

        // generate response
        AlbumDto albumDto = albumMapper.albumToAlbumDto(album);
        ResponseDto responseDto = new ResponseDto(HttpStatus.OK.toString(), "Album created successfully");

        return new AlbumResponseDto(albumDto, responseDto);
    }

    @Override
    public AlbumResponseDto updateAlbum(AlbumDto albumDto, String username) {
        logger.info("UPDATE ALBUM");
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User", "username", username);
        }

        // Imp: we need to do findByAlbumIdAndUser because by chance some other albumId is passed which of some other user and if we do findById on albumId
        // and then update, then some other user's album name will get updated.

        Optional<Album> optionalAlbum = albumRepository.findByAlbumIdAndUser(albumDto.getAlbumId(), optionalUser.get());
        if (optionalAlbum.isEmpty()) {
            throw new ResourceNotFoundException("Album", "albumId", albumDto.getAlbumId().toString());
        }

        Album album = optionalAlbum.get();
        //check if the album name is "default" then don't update
        if (Objects.equals(album.getAlbumName(), "default")) {
            throw new CannotPerformOperationException("UPDATE", "album", "albumName", album.getAlbumName());
        }

        // update album name
        album.setAlbumName(albumDto.getAlbumName());

        // save updated album in db
        album = albumRepository.save(album);

        // generate response
        albumDto = albumMapper.albumToAlbumDto(album);
        ResponseDto responseDto = new ResponseDto(HttpStatus.OK.toString(), "Album updated successfully");

        return new AlbumResponseDto(albumDto, responseDto);
    }

    @Override
    public ResponseDto deleteAlbum(Integer albumId, String username) {
        logger.info("DELETE ALBUM");
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User", "username", username);
        }

        Optional<Album> optionalAlbum = albumRepository.findByAlbumIdAndUser(albumId, optionalUser.get());
        if (optionalAlbum.isEmpty()) {
            throw new ResourceNotFoundException("Album", "albumId", albumId.toString());
        }

        Album album = optionalAlbum.get();

        //check if the album name is "default" then don't delete
        if (Objects.equals(album.getAlbumName(), "default")) {
            throw new CannotPerformOperationException("DELETE", "album", "albumName", album.getAlbumName());
        }

        logger.info("ALBUM TO DELETE: " + album.getAlbumId() + ", " + album.getAlbumName() + ", " + album.getUser().getUserId());

        // delete
        // you need to remove cascade type PERSIST from  parent entity to below line to work
        // https://stackoverflow.com/questions/29172313/spring-data-repository-does-not-delete-manytoone-entity
        albumRepository.delete(album);

        // generate response
        return new ResponseDto(HttpStatus.OK.toString(), "Album deleted successfully");
    }

    @Override
    public PhotoListDto getAllPhotos(Integer albumId, String username) {
        logger.info("GET ALL PHOTOS");
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User", "username", username);
        }

        Optional<Album> optionalAlbum = albumRepository.findByAlbumIdAndUser(albumId, optionalUser.get());
        if (optionalAlbum.isEmpty()) {
            throw new ResourceNotFoundException("Album", "albumId", albumId.toString());
        }

        return albumMapper.albumToPhotoListDto(optionalAlbum.get());
    }
}
