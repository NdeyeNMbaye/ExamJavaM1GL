package com.groupeisi.examm1gl.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClasseDto implements Serializable {

    private Integer id;
    private String className;
    private String description;

    // Informations du secteur pour éviter le lazy loading et les DTO imbriqués
    private Integer idSector;
    private String sectorName;
    private SectorDto sector;
}