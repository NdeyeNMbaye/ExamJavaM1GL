package com.groupeisi.examm1gl.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Data Transfer Object (DTO) pour la classe.
 * Utilisé pour transférer les données entre les couches de l'application
 * et pour lier les données du formulaire.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClasseDto implements Serializable {

    private Integer id;

    // Le nom de la classe ne doit pas être vide.
    @NotEmpty(message = "Le nom de la classe ne peut pas être vide")
    private String className;

    // La description ne doit pas être vide.
    @NotEmpty(message = "La description ne peut pas être vide")
    private String description;

    // Nous utilisons l'ID du secteur pour lier le formulaire au DTO.
    // L'objet SectorDto complet sera géré par la couche Service/DAO.
    private Integer idSector;

    // Ajout du nom du secteur pour l'affichage dans la liste
    private String sectorName;
}
