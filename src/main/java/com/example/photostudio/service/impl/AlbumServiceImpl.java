package com.example.photostudio.service.impl;

import com.example.photostudio.dto.AlbumDto;
import com.example.photostudio.dto.AlbumRequestDto;
import com.example.photostudio.dto.AlbumResponseDto;
import com.example.photostudio.dto.ResponseDto;
import com.example.photostudio.entity.Album;
import com.example.photostudio.entity.User;
import com.example.photostudio.exception.ResourceNotFoundException;
import com.example.photostudio.mapper.AlbumMapper;
import com.example.photostudio.repository.AlbumRepository;
import com.example.photostudio.repository.UserRepository;
import com.example.photostudio.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
}