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
    public SectorDto get(Integer id) {
        return sectorDao.findById(id)
                .map(sectorMapper::toSectorDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        messageSource.getMessage("sector.notfound", new Object[]{id}, Locale.getDefault())
                ));
    }

    @Override
    @Transactional
    public SectorDto save(SectorDto sectorDto) {
        SectorEntity sector = sectorMapper.toSectorEntity(sectorDto);
        return sectorMapper.toSectorDto(sectorDao.save(sector));
    }

    @Override
    @Transactional
    public SectorDto update(SectorDto sectorDto) {
        SectorEntity existingSector = sectorDao.findById(sectorDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        messageSource.getMessage("sector.notfound", new Object[]{sectorDto.getId()}, Locale.getDefault())
                ));

        existingSector.setName(sectorDto.getName());

        return sectorMapper.toSectorDto(sectorDao.save(existingSector));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!sectorDao.existsById(id)) {
            throw new EntityNotFoundException(
                    messageSource.getMessage("sector.notfound", new Object[]{id}, Locale.getDefault())
            );
        }
        sectorDao.deleteById(id);
    }
}