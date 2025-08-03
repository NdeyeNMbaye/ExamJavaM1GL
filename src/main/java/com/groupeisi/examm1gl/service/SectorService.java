package com.groupeisi.examm1gl.service;

import com.groupeisi.examm1gl.dao.ISectorDao;
import com.groupeisi.examm1gl.dto.SectorDto;
import com.groupeisi.examm1gl.entity.SectorEntity;
import com.groupeisi.examm1gl.exception.EntityNotFoundException;
import com.groupeisi.examm1gl.mapper.SectorMapper;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
@Setter
public class SectorService implements IUSectorSrvice {

    private ISectorDao sectorDao;
    private SectorMapper sectorMapper;
    private MessageSource messageSource;

    @Override
    @Transactional(readOnly = true)
    public List<SectorDto> getAll() {
        List<SectorEntity> sectors = sectorDao.findAll();
        return sectorMapper.listSectorEntityToListSectorDto(sectors);
    }

    @Override
    @Transactional(readOnly = true)
    public SectorDto get(int id) {
        return sectorDao.findById(id)
                .map(sectorMapper::toSectorDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        messageSource.getMessage("sector.notfound", new Object[]{id}, Locale.getDefault())
                ));
    }

    /**
     * Récupère un secteur par son nom.
     * @param nom Le nom du secteur.
     * @return L'objet SectorDto correspondant.
     */
    @Override
    @Transactional(readOnly = true)
    public SectorDto getSectorByName(String nom) {
        return sectorDao.findByName(nom)
                .map(sectorMapper::toSectorDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        messageSource.getMessage("sector.notfound.byName", new Object[]{nom}, Locale.getDefault())
                ));
    }

    @Override
    @Transactional
    public SectorDto add(SectorDto sector) {
        SectorEntity sectorEntity = sectorMapper.toSectorEntity(sector);
        return sectorMapper.toSectorDto(sectorDao.save(sectorEntity));
    }

    @Override
    @Transactional
    public SectorDto update(SectorDto sector) {
        SectorEntity existingSector = sectorDao.findById(sector.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        messageSource.getMessage("sector.notfound", new Object[]{sector.getId()}, Locale.getDefault())
                ));

        existingSector.setName(sector.getName());

        return sectorMapper.toSectorDto(sectorDao.save(existingSector));
    }

    @Override
    @Transactional
    public void delete(int id) {
        if (!sectorDao.existsById(id)) {
            throw new EntityNotFoundException(
                    messageSource.getMessage("sector.notfound", new Object[]{id}, Locale.getDefault())
            );
        }
        sectorDao.deleteById(id);
    }
}
