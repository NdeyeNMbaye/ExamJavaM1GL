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

/**
 * Cette classe est un Data Access Object (DAO) pour l'entité {@link ClasseEntity}.
 * Elle utilise l'API Criteria de JPA pour construire dynamiquement des requêtes.
 */
@Repository
@AllArgsConstructor
public class ClasseDao {

    private EntityManager em;

    /**
     * Recherche une classe par son nom.
     * Cette méthode construit une requête Criteria pour trouver l'entité
     * dont l'attribut `className` correspond à la chaîne de caractères fournie.
     *
     * @param className Le nom de la classe à rechercher.
     * @return Un {@link Optional} contenant l'entité trouvée ou un {@link Optional#empty()}
     * si aucune entité ne correspond ou si une exception survient.
     */
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

    /**
     * Récupère toutes les classes et les trie par nom de classe par ordre croissant.
     * Cette méthode construit une requête Criteria pour sélectionner toutes les
     * entités {@link ClasseEntity} et les ordonne par l'attribut `className`.
     *
     * @return Un {@link Optional} contenant une liste de toutes les classes triées,
     * ou un {@link Optional#empty()} si la liste est nulle.
     */
    public Optional<List<ClasseEntity>> allClassesOrderByClassName() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ClasseEntity> cr = cb.createQuery(ClasseEntity.class);
        Root<ClasseEntity> classe = cr.from(ClasseEntity.class);

        cr.orderBy(cb.asc(classe.get("className")));
        cr.select(classe);

        return Optional.ofNullable(em.createQuery(cr).getResultList());
    }

    /**
     * Recherche toutes les classes appartenant à un secteur donné.
     * Cette méthode navigue à travers la relation `classe -> sector -> id`
     * pour trouver toutes les classes liées à l'identifiant de secteur fourni.
     *
     * @param sectorId L'identifiant du secteur.
     * @return Un {@link Optional} contenant une liste des classes trouvées
     * ou un {@link Optional#empty()} si la liste est nulle.
     */
    public Optional<List<ClasseEntity>> findBySectorId(int sectorId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<ClasseEntity> cr = cb.createQuery(ClasseEntity.class);
        Root<ClasseEntity> classe = cr.from(ClasseEntity.class);

        Predicate predicate = cb.equal(classe.get("sector").get("id"), sectorId);
        cr.select(classe);
        cr.where(predicate);

        return Optional.ofNullable(em.createQuery(cr).getResultList());
    }

    /**
     * Compte le nombre total de classes dans la base de données.
     * Cette méthode utilise une requête Criteria pour effectuer une opération de
     * comptage (`cb.count`) sur l'entité {@link ClasseEntity}.
     *
     * @return Un {@link Optional} contenant le nombre total de classes,
     * ou un {@link Optional#empty()} si le résultat est nul.
     */
    public Optional<Long> countAllClasses() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cr = cb.createQuery(Long.class);
        Root<ClasseEntity> classe = cr.from(ClasseEntity.class);

        cr.select(cb.count(classe));

        return Optional.ofNullable(em.createQuery(cr).getSingleResult());
    }
}