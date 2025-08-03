package com.groupeisi.examm1gl.dao;

import com.groupeisi.examm1gl.entity.ClasseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IClasseDao extends JpaRepository<ClasseEntity, Integer> {

    // Méthode pour trouver une classe par son nom
    Optional<ClasseEntity> findByClassName(String className);

    // Méthode pour trouver toutes les classes d'un secteur par son ID
    List<ClasseEntity> findBySectorId(Integer sectorId);
}