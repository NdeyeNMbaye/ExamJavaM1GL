package com.groupeisi.examm1gl.controller;

import com.groupeisi.examm1gl.dto.ClasseDto;
import com.groupeisi.examm1gl.exception.EntityNotFoundException;
import com.groupeisi.examm1gl.service.IUClasseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/classes")
public class ClasseController {

    private final IUClasseService classeService;

    @Autowired
    public ClasseController(IUClasseService classeService) {
        this.classeService = classeService;
    }

    @GetMapping
    public ResponseEntity<List<ClasseDto>> getAllClasses() {
        List<ClasseDto> classes = classeService.getAll();
        return ResponseEntity.ok(classes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClasseDto> getClasseById(@PathVariable Integer id) {
        try {
            ClasseDto classe = classeService.get(id);
            return ResponseEntity.ok(classe);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ClasseDto> saveClasse(@RequestBody ClasseDto classeDto) {
        try {
            // Le service retourne directement l'objet ClasseDto sauvegardé
            ClasseDto savedClasse = classeService.save(classeDto);
            return new ResponseEntity<>(savedClasse, HttpStatus.CREATED);
        } catch (Exception e) {
            // Gérer les autres exceptions comme les erreurs de base de données
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClasseDto> updateClasse(@PathVariable Integer id, @RequestBody ClasseDto classeDto) {
        try {
            classeDto.setId(id);
            // Le service retourne directement l'objet ClasseDto mis à jour
            ClasseDto updatedClasse = classeService.update(classeDto);
            return ResponseEntity.ok(updatedClasse);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClasse(@PathVariable Integer id) {
        try {
            classeService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}