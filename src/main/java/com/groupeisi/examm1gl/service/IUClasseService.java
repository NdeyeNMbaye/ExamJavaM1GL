package com.groupeisi.examm1gl.service;

import com.groupeisi.examm1gl.dto.ClasseDto;
import com.groupeisi.examm1gl.exception.EntityNotFoundException;

import java.util.List;

/**
 * Interface de service pour la gestion des classes.
 * Elle définit le contrat des opérations CRUD (Create, Read, Update, Delete)
 * pour l'entité {@link com.groupeisi.examm1gl.entity.ClasseEntity}.
 */
public interface IUClasseService {
    /**
     * Récupère toutes les classes disponibles.
     * @return Une liste d'objets {@link ClasseDto}.
     */
    public List<ClasseDto> getAll();

    /**
     * Récupère une classe par son identifiant unique.
     * @param id L'identifiant de la classe à récupérer.
     * @return L'objet {@link ClasseDto} correspondant à l'identifiant.
     * @throws EntityNotFoundException si aucune classe n'est trouvée avec cet identifiant.
     */
    public ClasseDto get(Integer id);

    /**
     * Enregistre une nouvelle classe.
     * @param classeDto L'objet {@link ClasseDto} contenant les données de la nouvelle classe.
     * @return L'objet {@link ClasseDto} de la classe nouvellement créée.
     * @throws EntityNotFoundException si le secteur associé n'est pas trouvé.
     */
    public ClasseDto save(ClasseDto classeDto);

    /**
     * Met à jour une classe existante.
     * @param classeDto L'objet {@link ClasseDto} contenant les données mises à jour.
     * @return L'objet {@link ClasseDto} de la classe modifiée.
     * @throws EntityNotFoundException si la classe ou le secteur associé n'est pas trouvé.
     */
    public ClasseDto update(ClasseDto classeDto);

    /**
     * Supprime une classe par son identifiant unique.
     * @param id L'identifiant de la classe à supprimer.
     * @throws EntityNotFoundException si la classe n'est pas trouvée.
     */
    public void delete(Integer id);
}