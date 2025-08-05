package com.groupeisi.examm1gl.controller;

import com.groupeisi.examm1gl.dto.SectorDto;
import com.groupeisi.examm1gl.exception.EntityNotFoundException;
import com.groupeisi.examm1gl.service.IUSectorSrvice;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Ce contrôleur gère les requêtes liées à la gestion des secteurs.
 * Il propose des endpoints pour les vues (Thymeleaf) et une API REST.
 */
@Controller
@RequestMapping("/sectors")
public class SectorController {

    private final IUSectorSrvice sectorService;

    /**
     * Constructeur pour l'injection de la dépendance du service Sector.
     * @param sectorService Le service pour gérer la logique métier des secteurs.
     */
    @Autowired
    public SectorController(IUSectorSrvice sectorService) {
        this.sectorService = sectorService;
    }

    /**
     * Affiche la page de la liste de tous les secteurs.
     * @param model Le modèle pour passer la liste des secteurs à la vue.
     * @return Le nom de la vue "sector/liste".
     */
    @GetMapping("/liste")
    public String viewSectorsPage(Model model) {
        model.addAttribute("sectors", sectorService.getAll());
        return "sector/liste";
    }

    /**
     * Affiche le formulaire pour ajouter un nouveau secteur.
     * @param model Le modèle pour ajouter un objet SectorDto vide à la vue.
     * @return Le nom de la vue "sector/ajout".
     */
    @GetMapping("/ajout")
    public String showAddForm(Model model) {
        model.addAttribute("sector", new SectorDto());
        return "sector/ajout";
    }

    /**
     * Traite la soumission du formulaire pour la création ou la mise à jour d'un secteur.
     * @param sectorDto L'objet SectorDto à valider et sauvegarder.
     * @param result Les résultats de la validation du formulaire.
     * @param redirectAttributes Attributs pour les messages de succès ou d'erreur après redirection.
     * @return Une redirection vers la liste des secteurs en cas de succès ou le formulaire en cas d'erreur.
     */
    @PostMapping("/save")
    public String saveSector(@Valid @ModelAttribute("sector") SectorDto sectorDto, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "sector/ajout";
        }
        try {
            if (sectorDto.getId() != null) {
                // Si l'ID existe, c'est une mise à jour
                sectorService.update(sectorDto);
                redirectAttributes.addFlashAttribute("message", "Secteur modifié avec succès !");
            } else {
                // Sinon, c'est une création
                sectorService.add(sectorDto);
                redirectAttributes.addFlashAttribute("message", "Secteur ajouté avec succès !");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Une erreur est survenue lors de l'enregistrement.");
        }
        return "redirect:/sectors/liste";
    }

    /**
     * Affiche le formulaire de modification pour un secteur existant.
     * @param id L'identifiant du secteur à modifier.
     * @param model Le modèle pour ajouter l'objet SectorDto à la vue.
     * @param redirectAttributes Attributs pour un message d'erreur si le secteur n'est pas trouvé.
     * @return Le nom de la vue "sector/modifie" ou une redirection vers la liste.
     */
    @GetMapping("/modifie/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            SectorDto sector = sectorService.get(id);
            model.addAttribute("sector", sector);
            return "sector/modifie";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Secteur non trouvé.");
            return "redirect:/sectors/liste";
        }
    }

    /**
     * Supprime un secteur en utilisant son identifiant.
     * @param id L'identifiant du secteur à supprimer.
     * @param redirectAttributes Attributs pour les messages de succès ou d'erreur.
     * @return Une redirection vers la liste des secteurs.
     */
    @GetMapping("/supprime/{id}")
    public String deleteSector(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            sectorService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Secteur supprimé avec succès !");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Secteur non trouvé.");
        }
        return "redirect:/sectors/liste";
    }

    // --- Début des endpoints de l'API REST ---

    /**
     * Récupère la liste de tous les secteurs via une API REST.
     * @return Une ResponseEntity contenant la liste des secteurs.
     */
    @GetMapping("/api/sectors")
    @ResponseBody
    public ResponseEntity<List<SectorDto>> getAllSectors() {
        List<SectorDto> sectors = sectorService.getAll();
        return ResponseEntity.ok(sectors);
    }

    /**
     * Récupère un secteur par son identifiant via une API REST.
     * @param id L'identifiant du secteur.
     * @return Une ResponseEntity contenant le secteur ou un statut 404 si non trouvé.
     * @throws EntityNotFoundException Si aucun secteur n'est trouvé avec l'identifiant donné.
     */
    @GetMapping("/api/sectors/{id}")
    @ResponseBody
    public ResponseEntity<SectorDto> getSectorById(@PathVariable Integer id) {
        try {
            SectorDto sector = sectorService.get(id);
            return ResponseEntity.ok(sector);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Sauvegarde un nouveau secteur via une API REST.
     * @param sectorDto L'objet SectorDto à sauvegarder.
     * @return Une ResponseEntity contenant le secteur sauvegardé avec un statut 201 (CREATED) ou 400 (BAD REQUEST) en cas d'erreur.
     */
    @PostMapping("/api/sectors")
    @ResponseBody
    public ResponseEntity<SectorDto> saveSectorApi(@RequestBody SectorDto sectorDto) {
        try {
            SectorDto savedSector = sectorService.add(sectorDto);
            return new ResponseEntity<>(savedSector, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Met à jour un secteur existant via une API REST.
     * @param id L'identifiant du secteur à mettre à jour.
     * @param sectorDto L'objet SectorDto avec les données mises à jour.
     * @return Une ResponseEntity contenant le secteur mis à jour ou un statut 404 (NOT FOUND) ou 400 (BAD REQUEST) en cas d'erreur.
     */
    @PutMapping("/api/sectors/{id}")
    @ResponseBody
    public ResponseEntity<SectorDto> updateSectorApi(@PathVariable Integer id, @RequestBody SectorDto sectorDto) {
        try {
            sectorDto.setId(id);
            SectorDto updatedSector = sectorService.update(sectorDto);
            return ResponseEntity.ok(updatedSector);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Supprime un secteur par son identifiant via une API REST.
     * @param id L'identifiant du secteur à supprimer.
     * @return Une ResponseEntity avec un statut 204 (NO CONTENT) si la suppression réussit ou 404 (NOT FOUND) si le secteur n'existe pas.
     */
    @DeleteMapping("/api/sectors/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteSectorApi(@PathVariable Integer id) {
        try {
            sectorService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}