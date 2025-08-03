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

@Controller
@RequestMapping("/classes")
public class ClasseController {

    private final IUClasseService classeService;
    private final IUSectorSrvice sectorService;

    @Autowired
    public ClasseController(IUClasseService classeService, IUSectorSrvice sectorService) {
        this.classeService = classeService;
        this.sectorService = sectorService;
    }

    /**
     * Gère la requête GET pour afficher la page de la liste des classes.
     * @param model Le modèle pour passer les données à la vue.
     * @return Le nom de la vue à afficher (e.g., "classe/liste").
     */
    @GetMapping("/liste")
    public String viewClassesPage(Model model) {
        model.addAttribute("classes", classeService.getAll());
        return "classe/liste";
    }

    /**
     * Gère la requête GET pour afficher le formulaire d'ajout d'une classe.
     * @param model Le modèle pour ajouter un objet ClasseDto vide.
     * @return Le nom de la vue du formulaire d'ajout.
     */
    @GetMapping("/ajout")
    public String showAddForm(Model model) {
        model.addAttribute("classe", new ClasseDto());
        List<SectorDto> sectors = sectorService.getAll();
        model.addAttribute("sectors", sectors);
        return "classe/ajout";
    }

    /**
     * Gère la soumission du formulaire d'ajout d'une nouvelle classe.
     * @param classeDto L'objet ClasseDto à enregistrer.
     * @param result Les résultats de la validation.
     * @param model Le modèle pour repasser des données en cas d'erreur.
     * @param redirectAttributes Attributs pour passer des messages de redirection.
     * @return Une redirection vers la liste des classes.
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
     * Gère la requête GET pour afficher le formulaire de modification.
     * @param id L'identifiant de la classe à modifier.
     * @param model Le modèle pour ajouter la classe à la vue.
     * @param redirectAttributes Attributs pour la redirection en cas d'erreur.
     * @return Le nom de la vue du formulaire de modification ou une redirection.
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
     * Gère la soumission du formulaire de modification d'une classe existante.
     * @param classeDto L'objet ClasseDto à mettre à jour.
     * @param result Les résultats de la validation.
     * @param model Le modèle pour repasser des données en cas d'erreur.
     * @param redirectAttributes Attributs pour passer des messages de redirection.
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
     * Gère la requête GET pour supprimer une classe.
     * @param id L'identifiant de la classe à supprimer.
     * @param redirectAttributes Attributs pour la redirection en cas de succès ou d'erreur.
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

    // -- Début des endpoints de l'API REST pour interagir avec JavaScript --

    @GetMapping("/api/classes")
    @ResponseBody
    public ResponseEntity<List<ClasseDto>> getAllClasses() {
        List<ClasseDto> classes = classeService.getAll();
        return ResponseEntity.ok(classes);
    }

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
