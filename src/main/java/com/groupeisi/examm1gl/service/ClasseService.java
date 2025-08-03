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

    // Injection des dépendances
    private IClasseDao classeDao;
    private ISectorDao sectorDao;
    private ClasseMapper classeMapper;
    private MessageSource messageSource;

    /**
     * Récupère la liste de toutes les classes.
     * @return une liste de ClasseDto.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ClasseDto> getAll() {
        List<ClasseEntity> classes = classeDao.findAll();
        return classeMapper.listClasseEntityToListClasseDto(classes);
    }

    /**
     * Récupère une classe par son identifiant.
     * @param id l'identifiant de la classe.
     * @return un objet ClasseDto.
     * @throws EntityNotFoundException si la classe n'est pas trouvée.
     */
    @Override
    @Transactional(readOnly = true)
    public ClasseDto get(Integer id) {
        return classeDao.findById(id)
                .map(classeMapper::toClasseDto)
                .orElseThrow(() -> new EntityNotFoundException(
                        messageSource.getMessage("classe.notfound", new Object[]{id}, Locale.getDefault())
                ));
    }

    /**
     * Enregistre une nouvelle classe.
     * Gère la relation avec le secteur associé.
     * @param classeDto l'objet ClasseDto à enregistrer.
     * @return l'objet ClasseDto après l'enregistrement.
     * @throws EntityNotFoundException si le secteur n'est pas trouvé.
     */
    @Override
    @Transactional
    public ClasseDto save(ClasseDto classeDto) {
        // Recherche du secteur associé
        SectorEntity sector = sectorDao.findById(classeDto.getIdSector())
                .orElseThrow(() -> new EntityNotFoundException(
                        messageSource.getMessage("sector.notfound", new Object[]{classeDto.getIdSector()}, Locale.getDefault())
                ));

        ClasseEntity classe = classeMapper.toClasseEntity(classeDto);
        classe.setSector(sector); // Définir l'entité complète SectorEntity

        return classeMapper.toClasseDto(classeDao.save(classe));
    }

    /**
     * Met à jour une classe existante.
     * @param classeDto l'objet ClasseDto avec les données mises à jour.
     * @return l'objet ClasseDto après la mise à jour.
     * @throws EntityNotFoundException si la classe ou le secteur n'est pas trouvé.
     */
    @Override
    @Transactional
    public ClasseDto update(ClasseDto classeDto) {
        // Vérification de l'existence de la classe
        ClasseEntity existingClasse = classeDao.findById(classeDto.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        messageSource.getMessage("classe.notfound", new Object[]{classeDto.getId()}, Locale.getDefault())
                ));

        // Recherche du secteur associé
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

    /**
     * Supprime une classe par son identifiant.
     * @param id l'identifiant de la classe à supprimer.
     */
    @Override
    @Transactional
    public void delete(Integer id) {
        classeDao.deleteById(id);
    }
}
