package com.groupeisi.examm1gl.mapper;

import com.groupeisi.examm1gl.dto.ClasseDto;
import com.groupeisi.examm1gl.entity.ClasseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClasseMapper {

    @Mapping(source = "sector.id", target = "idSector")
    @Mapping(source = "sector.name", target = "sectorName")
    ClasseDto toClasseDto(ClasseEntity classeEntity);

    @Mapping(source = "idSector", target = "sector.id")
    ClasseEntity toClasseEntity(ClasseDto classeDto);

    List<ClasseDto> listClasseEntityToListClasseDto(List<ClasseEntity> classeEntities);
}