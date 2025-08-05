package com.groupeisi.examm1gl.controller;

import com.groupeisi.examm1gl.dto.ClasseDto;
import com.groupeisi.examm1gl.dto.SectorDto;
import com.groupeisi.examm1gl.exception.EntityNotFoundException;
import com.groupeisi.examm1gl.service.IUClasseService;
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
 * Ce contrôleur gère les requêtes liées à la gestion des classes.
 * Il offre des endpoints pour les vues (Thymeleaf) et une API REST pour les classes.
 */
@Controller
@RequestMapping("/classes")
public class ClasseController {

    private final IUClasseService classeService;
    private final IUSectorSrvice sectorService;

    /**
     * Constructeur pour l'injection des dépendances des services Classe et Sector.
     * @param classeService Le service pour gérer la logique métier des classes.
     * @param sectorService Le service pour gérer la logique métier des secteurs.
     */
    @Autowired
    public ClasseController(IUClasseService classeService, IUSectorSrvice sectorService) {
        this.classeService = classeService;
        this.sectorService = sectorService;
    }

    /**
     * Affiche la page de la liste de toutes les classes.
     * @param model Le modèle pour passer la liste des classes à la vue.
     * @return Le nom de la vue "classe/liste".
     */
    @GetMapping("/liste")
    public String viewClassesPage(Model model) {
        model.addAttribute("classes", classeService.getAll());
        return "classe/liste";
    }

    /**
     * Affiche le formulaire pour ajouter une nouvelle classe.
     * @param model Le modèle pour ajouter un objet ClasseDto vide et la liste des secteurs à la vue.
     * @return Le nom de la vue "classe/ajout".
     */
    @GetMapping("/ajout")
    public String showAddForm(Model model) {
        model.addAttribute("classe", new ClasseDto());
        List<SectorDto> sectors = sectorService.getAll();
        model.addAttribute("sectors", sectors);
        return "classe/ajout";
    }

    /**
     * Traite la soumission du formulaire d'ajout d'une classe.
     * @param classeDto L'objet ClasseDto à valider et sauvegarder.
     * @param result Les résultats de la validation du formulaire.
     * @param model Le modèle en cas d'erreur de validation.
     * @param redirectAttributes Attributs pour les messages de succès ou d'erreur après redirection.
     * @return Une redirection vers la liste des classes en cas de succès ou le formulaire en cas d'erreur.
     */
    @PostMapping("/ajout")
    public String saveClasse(@Valid @ModelAttribute("classe") ClasseDto classeDto, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<SectorDto> sectors = sectorService.getAll();
            model.addAttribute("sectors", sectors);
            return "classe/ajout";
        }
        try {
            classeService.save(classeDto);
            redirectAttributes.addFlashAttribute("message", "Classe ajoutée avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Une erreur est survenue lors de l'ajout.");
        }
        return "redirect:/classes/liste";
    }

    /**
     * Affiche le formulaire de modification pour une classe existante.
     * @param id L'identifiant de la classe à modifier.
     * @param model Le modèle pour ajouter l'objet ClasseDto et la liste des secteurs.
     * @param redirectAttributes Attributs pour un message d'erreur si la classe n'est pas trouvée.
     * @return Le nom de la vue "classe/modifie" ou une redirection vers la liste.
     */
    @GetMapping("/modifie/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            ClasseDto classe = classeService.get(id);
            model.addAttribute("classe", classe);
            List<SectorDto> sectors = sectorService.getAll();
            model.addAttribute("sectors", sectors);
            return "classe/modifie";
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Classe non trouvée.");
            return "redirect:/classes/liste";
        }
    }

    /**
     * Traite la soumission du formulaire de modification d'une classe.
     * @param classeDto L'objet ClasseDto avec les données mises à jour.
     * @param result Les résultats de la validation du formulaire.
     * @param model Le modèle en cas d'erreur de validation.
     * @param redirectAttributes Attributs pour les messages de succès ou d'erreur après redirection.
     * @return Une redirection vers la liste des classes.
     */
    @PostMapping("/modifie")
    public String updateClasse(@Valid @ModelAttribute("classe") ClasseDto classeDto, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            List<SectorDto> sectors = sectorService.getAll();
            model.addAttribute("sectors", sectors);
            return "classe/modifie";
        }
        try {
            classeService.update(classeDto);
            redirectAttributes.addFlashAttribute("message", "Classe modifiée avec succès !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Une erreur est survenue lors de la modification.");
        }
        return "redirect:/classes/liste";
    }

    /**
     * Supprime une classe en utilisant son identifiant.
     * @param id L'identifiant de la classe à supprimer.
     * @param redirectAttributes Attributs pour les messages de succès ou d'erreur.
     * @return Une redirection vers la liste des classes.
     */
    @GetMapping("/supprime/{id}")
    public String deleteClasse(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            classeService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Classe supprimée avec succès !");
        } catch (EntityNotFoundException e) {
            redirectAttributes.addFlashAttribute("error", "Classe non trouvée.");
        }
        return "redirect:/classes/liste";
    }

    // --- Début des endpoints de l'API REST ---

    /**
     * Récupère la liste de toutes les classes via une API REST.
     * @return Une ResponseEntity contenant la liste des classes.
     */
    @GetMapping("/api/classes")
    @ResponseBody
    public ResponseEntity<List<ClasseDto>> getAllClasses() {
        List<ClasseDto> classes = classeService.getAll();
        return ResponseEntity.ok(classes);
    }

    /**
     * Récupère une classe par son identifiant via une API REST.
     * @param id L'identifiant de la classe.
     * @return Une ResponseEntity contenant la classe ou un statut 404 si non trouvée.
     * @throws EntityNotFoundException Si aucune classe n'est trouvée avec l'identifiant donné.
     */
    @GetMapping("/api/classes/{id}")
    @ResponseBody
    public ResponseEntity<ClasseDto> getClasseById(@PathVariable Integer id) {
        try {
            ClasseDto classe = classeService.get(id);
            return ResponseEntity.ok(classe);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Sauvegarde une nouvelle classe via une API REST.
     * @param classeDto L'objet ClasseDto à sauvegarder.
     * @return Une ResponseEntity contenant la classe sauvegardée avec un statut 201 (CREATED) ou 400 (BAD REQUEST) en cas d'erreur.
     */
    @PostMapping("/api/classes")
    @ResponseBody
    public ResponseEntity<ClasseDto> saveClasseApi(@RequestBody ClasseDto classeDto) {
        try {
            ClasseDto savedClasse = classeService.save(classeDto);
            return new ResponseEntity<>(savedClasse, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Met à jour une classe existante via une API REST.
     * @param id L'identifiant de la classe à mettre à jour.
     * @param classeDto L'objet ClasseDto avec les données mises à jour.
     * @return Une ResponseEntity contenant la classe mise à jour ou un statut 404 (NOT FOUND) ou 400 (BAD REQUEST) en cas d'erreur.
     */
    @PutMapping("/api/classes/{id}")
    @ResponseBody
    public ResponseEntity<ClasseDto> updateClasseApi(@PathVariable Integer id, @RequestBody ClasseDto classeDto) {
        try {
            classeDto.setId(id);
            ClasseDto updatedClasse = classeService.update(classeDto);
            return ResponseEntity.ok(updatedClasse);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Supprime une classe par son identifiant via une API REST.
     * @param id L'identifiant de la classe à supprimer.
     * @return Une ResponseEntity avec un statut 204 (NO CONTENT) si la suppression réussit ou 404 (NOT FOUND) si la classe n'existe pas.
     */
    @DeleteMapping("/api/classes/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteClasseApi(@PathVariable Integer id) {
        try {
            classeService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}