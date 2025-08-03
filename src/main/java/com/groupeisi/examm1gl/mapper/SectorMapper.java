package com.groupeisi.examm1gl.mapper;

import com.groupeisi.examm1gl.dto.SectorDto;
import com.groupeisi.examm1gl.entity.SectorEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SectorMapper {

    SectorDto toSectorDto(SectorEntity sectorEntity);
    SectorEntity toSectorEntity(SectorDto sectorDto);
    List<SectorDto> listSectorEntityToListSectorDto(List<SectorEntity> sectorEntities);
}