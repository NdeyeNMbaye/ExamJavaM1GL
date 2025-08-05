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

/**
 * Implémentation du service pour la gestion des secteurs.
 * Cette classe gère la logique métier des opérations CRUD pour les secteurs.
 */
@Service
@AllArgsConstructor
@Setter
public class SectorService implements IUSectorSrvice {

    /**
     * DAO pour l'accès aux données des secteurs.
     */
    private ISectorDao sectorDao;
    /**
     * Mapper pour la conversion entre les entités et les DTO de secteur.
     */
    private SectorMapper sectorMapper;
    /**
     * Source de messages pour la gestion des messages d'erreur internationalisés.
     */
    private MessageSource messageSource;

    /**
     * Récupère la liste de tous les secteurs.
     * @return Une liste d'objets {@link SectorDto}.
     */
    @Override
    @Transactional(readOnly = true)
    public List<SectorDto> getAll() {
        List<SectorEntity> sectors = sectorDao.findAll();
        return sectorMapper.listSectorEntityToListSectorDto(sectors);
    }

    /**
     * Récupère un secteur par son identifiant.
     * @param id L'identifiant du secteur à récupérer.
     * @return L'objet {@link SectorDto} correspondant.
     * @throws EntityNotFoundException si aucun secteur n'est trouvé pour l'identifiant donné.
     */
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
     * @param nom Le nom du secteur à rechercher.
     * @return L'objet {@link SectorDto} correspondant.
     * @throws EntityNotFoundException si aucun secteur n'est trouvé avec ce nom.
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

    /**
     * Ajoute un nouveau secteur.
     * @param sector L'objet {@link SectorDto} contenant les données du nouveau secteur.
     * @return L'objet {@link SectorDto} du secteur nouvellement créé.
     */
    @Override
    @Transactional
    public SectorDto add(SectorDto sector) {
        SectorEntity sectorEntity = sectorMapper.toSectorEntity(sector);
        return sectorMapper.toSectorDto(sectorDao.save(sectorEntity));
    }

    /**
     * Met à jour un secteur existant.
     * @param sector L'objet {@link SectorDto} avec les données mises à jour.
     * @return L'objet {@link SectorDto} du secteur modifié.
     * @throws EntityNotFoundException si le secteur n'est pas trouvé.
     */
    @Override
    @Transactional
    public SectorDto update(SectorDto sector) {
        // Vérifie si le secteur existe avant de le mettre à jour.
        SectorEntity existingSector = sectorDao.findById(sector.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        messageSource.getMessage("sector.notfound", new Object[]{sector.getId()}, Locale.getDefault())
                ));

        existingSector.setName(sector.getName());

        return sectorMapper.toSectorDto(sectorDao.save(existingSector));
    }

    /**
     * Supprime un secteur par son identifiant.
     * @param id L'identifiant du secteur à supprimer.
     * @throws EntityNotFoundException si le secteur n'est pas trouvé.
     */
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