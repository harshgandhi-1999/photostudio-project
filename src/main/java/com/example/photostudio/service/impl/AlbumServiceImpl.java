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
    public AlbumResponseDto createNewAlbum(String username, AlbumRequestDto albumRequestDto) {
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
        // album = albumRepository.save(album);

        // Previously we were doing above step but we can also do below step
        // save user in db , album will be automatically created as CascadeType.ALL is there
        userRepository.save(user);

        // generate response
        AlbumDto albumDto = albumMapper.albumToAlbumDto(album);
        ResponseDto responseDto = new ResponseDto(HttpStatus.OK.toString(), "Album created successfully");

        return new AlbumResponseDto(albumDto, responseDto);
    }

    @Override
    public AlbumResponseDto updateAlbum(String username, AlbumDto albumDto) {
        logger.info("UPDATE ALBUM");
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User", "username", username);
        }
        User user = optionalUser.get();

        Optional<Album> optionalAlbum = albumRepository.findById(albumDto.getAlbumId());

        if (optionalAlbum.isEmpty()) {
            throw new ResourceNotFoundException("Album", "albumId", albumDto.getAlbumId().toString());
        }

        Album albumToUpdate = optionalAlbum.get();

        if (!albumToUpdate.getUser().getUserId().equals(user.getUserId())) {
            throw new CannotPerformOperationException("UPDATE", "album", "albumName", "Album doesn't belong to you");
        }

        //check if the album name is "default" then don't update
        if (Objects.equals(albumToUpdate.getAlbumName(), "default")) {
            throw new CannotPerformOperationException("UPDATE", "album", "albumName", "You cannot update 'default' album");
        }

        // update album name
        albumToUpdate.setAlbumName(albumDto.getAlbumName());

        // save updated album in db
        albumRepository.save(albumToUpdate);

        // generate response
        albumDto = albumMapper.albumToAlbumDto(albumToUpdate);
        ResponseDto responseDto = new ResponseDto(HttpStatus.OK.toString(), "Album updated successfully");

        return new AlbumResponseDto(albumDto, responseDto);
    }

    @Override
    public ResponseDto deleteAlbum(String username, Integer albumId) {
        logger.info("DELETE ALBUM");
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User", "username", username);
        }

        User user = optionalUser.get();

//        Which approach to prefer - iterate over album list or query albumRepostiory
//        ✅ Small album list? Use user.getAlbums().stream() — safer, single query.
//
//        ✅ Large album list? Use albumRepository.findById() — more efficient.
//
//        ✅ Use orphanRemoval inside User entity? Prefer user.getAlbums().remove(album) with setUser(null).

        Optional<Album> optionalAlbum = albumRepository.findById(albumId);

        //Optional<Album> albumToRemove = user.getAlbums().stream().filter(x -> Objects.equals(x.getAlbumId(), albumId)).findFirst();

        if (optionalAlbum.isEmpty()) {
            throw new ResourceNotFoundException("Album", "albumId", albumId.toString());
        }

        Album albumToRemove = optionalAlbum.get();

        if (!albumToRemove.getUser().getUserId().equals(user.getUserId())) {
            throw new CannotPerformOperationException("DELETE", "album", "albumName", "Album doesn't belong to you");
        }

        if (Objects.equals(albumToRemove.getAlbumName(), "default")) {
            throw new CannotPerformOperationException("DELETE", "album", "albumName", "default");
        }

        if(!albumToRemove.getPhotos().isEmpty()){
            throw new CannotPerformOperationException("DELETE","album","albumName","Album is not empty");
        }

        // remove album from user's album list

        // cleaner code below line - instead of two lines here move this logic to user entity
        user.removeAlbum(albumToRemove);
        // albumToRemove.setUser(null);
        // user.getAlbums().remove(albumToRemove);

        // cascading will delete the album from album repo after saving user
        userRepository.save(user);

        // generate response
        return new ResponseDto(HttpStatus.OK.toString(), "Album deleted successfully");
    }

    @Override
    public PhotoListDto getAllPhotos(String username, Integer albumId) {
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
