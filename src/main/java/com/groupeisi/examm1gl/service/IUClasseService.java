package com.groupeisi.examm1gl.service;

import com.groupeisi.examm1gl.dto.ClasseDto;

import java.util.List;

public interface IUClasseService {
    List<ClasseDto> getAll();
    ClasseDto get(Integer id);
    ClasseDto save(ClasseDto classeDto);
    ClasseDto update(ClasseDto classeDto);
    void delete(Integer id);
}