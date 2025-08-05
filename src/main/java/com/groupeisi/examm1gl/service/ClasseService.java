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

/**
 * Implémentation du service pour la gestion des classes.
 * Cette classe gère la logique métier et les transactions,
 * en coordonnant les interactions entre les contrôleurs, les mappers et les DAO.
 */
@Service
@AllArgsConstructor
@Setter
public class ClasseService implements IUClasseService {

    /**
     * DAO pour l'accès aux données des classes.
     */
    private IClasseDao classeDao;
    /**
     * DAO pour l'accès aux données des secteurs.
     */
    private ISectorDao sectorDao;
    /**
     * Mapper pour la conversion entre les entités et les DTO de classe.
     */
    private ClasseMapper classeMapper;
    /**
     * Source de messages pour la gestion des messages d'erreur internationalisés.
     */
    private MessageSource messageSource;

    /**
     * Récupère la liste de toutes les classes.
     * @return Une liste de {@link ClasseDto}.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ClasseDto> getAll() {
        List<ClasseEntity> classes = classeDao.findAll();
        return classeMapper.listClasseEntityToListClasseDto(classes);
    }

    /**
     * Récupère une classe par son identifiant.
     * @param id L'identifiant de la classe à récupérer.
     * @return L'objet {@link ClasseDto} correspondant.
     * @throws EntityNotFoundException si aucune classe n'est trouvée pour l'identifiant donné.
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
     * La méthode vérifie d'abord l'existence du secteur associé avant de
     * sauvegarder la classe.
     *
     * @param classeDto L'objet {@link ClasseDto} à enregistrer.
     * @return L'objet {@link ClasseDto} après l'enregistrement.
     * @throws EntityNotFoundException si le secteur spécifié n'existe pas.
     */
    @Override
    @Transactional
    public ClasseDto save(ClasseDto classeDto) {
        // Recherche du secteur associé pour s'assurer de son existence
        SectorEntity sector = sectorDao.findById(classeDto.getIdSector())
                .orElseThrow(() -> new EntityNotFoundException(
                        messageSource.getMessage("sector.notfound", new Object[]{classeDto.getIdSector()}, Locale.getDefault())
                ));

        ClasseEntity classe = classeMapper.toClasseEntity(classeDto);
        classe.setSector(sector);

        return classeMapper.toClasseDto(classeDao.save(classe));
    }

    /**
     * Met à jour une classe existante.
     * La méthode vérifie l'existence de la classe et du secteur associé avant de
     * procéder à la mise à jour des données.
     *
     * @param classeDto L'objet {@link ClasseDto} contenant les données mises à jour.
     * @return L'objet {@link ClasseDto} après la mise à jour.
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

        // Recherche du secteur associé pour s'assurer de son existence
        SectorEntity sector = sectorDao.findById(classeDto.getIdSector())
                .orElseThrow(() -> new EntityNotFoundException(
                        messageSource.getMessage("sector.notfound", new Object[]{classeDto.getIdSector()}, Locale.getDefault())
                ));

        // Mise à jour des propriétés de l'entité existante
        existingClasse.setClassName(classeDto.getClassName());
        existingClasse.setDescription(classeDto.getDescription());
        existingClasse.setSector(sector);

        return classeMapper.toClasseDto(classeDao.save(existingClasse));
    }

    /**
     * Supprime une classe par son identifiant.
     * @param id L'identifiant de la classe à supprimer.
     * @throws EntityNotFoundException si aucune classe n'est trouvée pour l'identifiant donné.
     */
    @Override
    @Transactional
    public void delete(Integer id) {
        classeDao.deleteById(id);
    }
}