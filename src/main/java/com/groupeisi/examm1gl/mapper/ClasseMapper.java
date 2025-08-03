package com.groupeisi.examm1gl.mapper;

import com.groupeisi.examm1gl.dto.ClasseDto;
import com.groupeisi.examm1gl.entity.ClasseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper pour la conversion entre ClasseEntity et ClasseDto,
 * configuré pour fonctionner avec Spring.
 */
@Mapper(componentModel = "spring")
public interface ClasseMapper {

    /**
     * Convertit une ClasseEntity en ClasseDto.
     * On mappe explicitement l'id du secteur de l'entité vers l'idSector du DTO.
     * Le mapping de la propriété 'name' du secteur est crucial pour l'affichage.
     */
    @Mapping(source = "sector.id", target = "idSector")
    // Correction : Le nom de la propriété du secteur est 'name', et non 'sectorName'
    @Mapping(source = "sector.name", target = "sectorName")
    ClasseDto toClasseDto(ClasseEntity classeEntity);

    /**
     * Convertit une ClasseDto en ClasseEntity.
     * On mappe explicitement l'id du secteur du DTO vers l'id du secteur de l'entité.
     * La logique de récupération de l'objet SectorEntity complet
     * sera gérée par la couche Service.
     */
    @Mapping(source = "idSector", target = "sector.id")
    ClasseEntity toClasseEntity(ClasseDto classeDto);

    /**
     * Convertit une liste de ClasseEntity en une liste de ClasseDto.
     */
    List<ClasseDto> listClasseEntityToListClasseDto(List<ClasseEntity> classeEntities);
}
