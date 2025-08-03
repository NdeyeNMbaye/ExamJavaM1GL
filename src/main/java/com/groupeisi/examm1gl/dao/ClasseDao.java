package com.groupeisi.examm1gl.dao;

import com.groupeisi.examm1gl.entity.ClasseEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class ClasseDao {

    private EntityManager em;

    public Optional<ClasseEntity> findByClassName(String className) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ClasseEntity> cr = cb.createQuery(ClasseEntity.class);
        Root<ClasseEntity> classe = cr.from(ClasseEntity.class);

        Predicate predicate = cb.equal(classe.get("className"), className);
        cr.select(classe);
        cr.where(predicate);

        try {
            return Optional.ofNullable(em.createQuery(cr).getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<List<ClasseEntity>> allClassesOrderByClassName() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ClasseEntity> cr = cb.createQuery(ClasseEntity.class);
        Root<ClasseEntity> classe = cr.from(ClasseEntity.class);

        cr.orderBy(cb.asc(classe.get("className")));
        cr.select(classe);

        return Optional.ofNullable(em.createQuery(cr).getResultList());
    }

    public Optional<List<ClasseEntity>> findBySectorId(int sectorId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ClasseEntity> cr = cb.createQuery(ClasseEntity.class);
        Root<ClasseEntity> classe = cr.from(ClasseEntity.class);

        Predicate predicate = cb.equal(classe.get("sector").get("id"), sectorId);
        cr.select(classe);
        cr.where(predicate);

        return Optional.ofNullable(em.createQuery(cr).getResultList());
    }

    public Optional<Long> countAllClasses() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cr = cb.createQuery(Long.class);
        Root<ClasseEntity> classe = cr.from(ClasseEntity.class);

        cr.select(cb.count(classe));

        return Optional.ofNullable(em.createQuery(cr).getSingleResult());
    }
}