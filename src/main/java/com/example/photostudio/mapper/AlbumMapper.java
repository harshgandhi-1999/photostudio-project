package com.example.photostudio.mapper;

import com.example.photostudio.dto.AlbumDto;
import com.example.photostudio.dto.AlbumListDto;
import com.example.photostudio.dto.PhotoListDto;
import com.example.photostudio.entity.Album;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AlbumMapper {
    AlbumDto albumToAlbumDto(Album album);

    PhotoListDto albumToPhotoListDto(Album album);
}
