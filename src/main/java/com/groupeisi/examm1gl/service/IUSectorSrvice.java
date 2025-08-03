package com.groupeisi.examm1gl.service;

import com.groupeisi.examm1gl.dto.SectorDto;
import java.util.List;

public interface IUSectorSrvice {
    List<SectorDto> getAll();
    SectorDto get(Integer id);
    SectorDto save(SectorDto sectorDto);
    SectorDto update(SectorDto sectorDto);
    void delete(Integer id); // Changé le retour en void
}