package com.groupeisi.examm1gl.dao;

import com.groupeisi.examm1gl.entity.SectorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISectorDao extends JpaRepository<SectorEntity, Integer> {

    // Méthode pour trouver un secteur par son nom
    Optional<SectorEntity> findByName(String name);
}