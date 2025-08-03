package com.groupeisi.examm1gl.service;

import com.groupeisi.examm1gl.dao.IClasseDao;
import com.groupeisi.examm1gl.dao.ISectorDao;
import com.groupeisi.examm1gl.dto.ClasseDto;
import com.groupeisi.examm1gl.entity.ClasseEntity;
import com.groupeisi.examm1gl.entity.SectorEntity;
import com.groupeisi.examm1gl.exception.EntityNotFoundException;
import com.groupeisi.examm1gl.mapper.ClasseMapper;
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
public class ClasseService implements IUClasseService {

    private IClasseDao classeDao;
    private ISectorDao sectorDao; // Ajouté pour gérer la relation
    private ClasseMapper classeMapper;
    private MessageSource messageSource;

    @Override
    @Transactional(readOnly = true)
    public List<ClasseDto> getAll() {
        List<ClasseEntity> classes = classeDao.findAll();
        return classeMapper.listClasseEntityToListClasseDto(classes);
    }

    @Override
    @Transactional(readOnly = true)
    public ClasseDto get(Integer id) {
        return classeDao.findById(id)
                .map(classeMapper::toClasseDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        messageSource.getMessage("classe.notfound", new Object[]{id}, Locale.getDefault())
                ));
    }

    @Override
    @Transactional
    public ClasseDto save(ClasseDto classeDto) {
        // Gérer la relation avec le secteur
        SectorEntity sector = sectorDao.findById(classeDto.getIdSector())
                .orElseThrow(() -> new EntityNotFoundException(
                        messageSource.getMessage("sector.notfound", new Object[]{classeDto.getIdSector()}, Locale.getDefault())
                ));

        ClasseEntity classe = classeMapper.toClasseEntity(classeDto);
        classe.setSector(sector); // Définir l'entité complète

        return classeMapper.toClasseDto(classeDao.save(classe));
    }

    @Override
    @Transactional
    public ClasseDto update(ClasseDto classeDto) {
        ClasseEntity existingClasse = classeDao.findById(classeDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        messageSource.getMessage("classe.notfound", new Object[]{classeDto.getId()}, Locale.getDefault())
                ));

        SectorEntity sector = sectorDao.findById(classeDto.getIdSector())
                .orElseThrow(() -> new EntityNotFoundException(
                        messageSource.getMessage("sector.notfound", new Object[]{classeDto.getIdSector()}, Locale.getDefault())
                ));

        // Mettre à jour les propriétés de l'entité existante
        existingClasse.setClassName(classeDto.getClassName());
        existingClasse.setDescription(classeDto.getDescription());
        existingClasse.setSector(sector);

        return classeMapper.toClasseDto(classeDao.save(existingClasse));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        classeDao.deleteById(id);
    }
}