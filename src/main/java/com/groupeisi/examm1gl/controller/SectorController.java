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

@Controller
@RequestMapping("/sectors")
public class SectorController {

    private final IUSectorSrvice sectorService;

    @Autowired
    public SectorController(IUSectorSrvice sectorService) {
        this.sectorService = sectorService;
    }

    /**
     * Gère la requête GET pour afficher la page de la liste des secteurs.
     */
    @GetMapping("/liste")
    public String viewSectorsPage(Model model) {
        model.addAttribute("sectors", sectorService.getAll());
        return "sector/liste";
    }

    /**
     * Gère la requête GET pour afficher le formulaire d'ajout d'un secteur.
     */
    @GetMapping("/ajout")
    public String showAddForm(Model model) {
        model.addAttribute("sector", new SectorDto());
        return "sector/ajout";
    }

    /**
     * Gère la soumission du formulaire d'ajout ou de modification.
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
                sectorService.add(sectorDto); // CORRECTION : Utilisation de la méthode add
                redirectAttributes.addFlashAttribute("message", "Secteur ajouté avec succès !");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Une erreur est survenue lors de l'enregistrement.");
        }
        return "redirect:/sectors/liste";
    }

    /**
     * Gère la requête GET pour afficher le formulaire de modification d'un secteur.
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
     * Gère la requête GET pour supprimer un secteur.
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

    // -- Début des endpoints de l'API REST --

    @GetMapping("/api/sectors")
    @ResponseBody
    public ResponseEntity<List<SectorDto>> getAllSectors() {
        List<SectorDto> sectors = sectorService.getAll();
        return ResponseEntity.ok(sectors);
    }

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

    @PostMapping("/api/sectors")
    @ResponseBody
    public ResponseEntity<SectorDto> saveSectorApi(@RequestBody SectorDto sectorDto) {
        try {
            SectorDto savedSector = sectorService.add(sectorDto); // CORRECTION : Utilisation de la méthode add
            return new ResponseEntity<>(savedSector, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

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
