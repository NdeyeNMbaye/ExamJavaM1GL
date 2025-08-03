package com.groupeisi.examm1gl.service;

import com.groupeisi.examm1gl.dto.SectorDto;

import java.util.List;

/**
 * Interface pour la gestion des services liés aux secteurs.
 */
public interface IUSectorSrvice {

    /**
     * Récupère la liste de tous les secteurs.
     * @return Une liste de SectorDto.
     */
    List<SectorDto> getAll();

    /**
     * Récupère un secteur par son ID.
     * @param id L'ID du secteur.
     * @return L'objet SectorDto correspondant.
     */
    SectorDto get(int id);

    /**
     * Récupère un secteur par son nom.
     * @param nom Le nom du secteur.
     * @return L'objet SectorDto correspondant.
     */
    SectorDto getSectorByName(String nom);

    /**
     * Ajoute un nouveau secteur.
     * @param sector L'objet SectorDto à ajouter.
     * @return L'objet SectorDto ajouté.
     */
    SectorDto add(SectorDto sector);

    /**
     * Met à jour un secteur existant.
     * @param sector L'objet SectorDto mis à jour.
     * @return L'objet SectorDto mis à jour.
     */
    SectorDto update(SectorDto sector);

    /**
     * Supprime un secteur par son ID.
     * @param id L'ID du secteur à supprimer.
     */
    void delete(int id);
}
