package com.groupeisi.examm1gl.service;

import com.groupeisi.examm1gl.dto.ClasseDto;
import com.groupeisi.examm1gl.exception.EntityNotFoundException;

import java.util.List;

/**
 * Interface pour le service de gestion des classes.
 * Elle définit les opérations CRUD (Create, Read, Update, Delete)
 * pour la ressource Classe.
 */
public interface IUClasseService {
    /**
     * Récupère la liste de toutes les classes, avec leurs secteurs associés.
     * @return une liste de ClasseDto.
     */
    public List<ClasseDto> getAll();

    /**
     * Récupère une classe par son identifiant.
     * @param id l'identifiant de la classe.
     * @return un objet ClasseDto.
     * @throws EntityNotFoundException si la classe n'est pas trouvée.
     */
    public ClasseDto get(Integer id);

    /**
     * Enregistre une nouvelle classe.
     * @param classeDto l'objet ClasseDto à enregistrer.
     * @return l'objet ClasseDto après l'enregistrement.
     * @throws EntityNotFoundException si le secteur associé n'est pas trouvé.
     */
    public ClasseDto save(ClasseDto classeDto);

    /**
     * Met à jour une classe existante.
     * @param classeDto l'objet ClasseDto avec les données mises à jour.
     * @return l'objet ClasseDto après la mise à jour.
     * @throws EntityNotFoundException si la classe ou le secteur n'est pas trouvé.
     */
    public ClasseDto update(ClasseDto classeDto);

    /**
     * Supprime une classe par son identifiant.
     * @param id l'identifiant de la classe à supprimer.
     * @throws EntityNotFoundException si la classe n'est pas trouvée.
     */
    public void delete(Integer id);
}
