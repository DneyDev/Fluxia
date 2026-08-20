package com.dneydev.fluxia.controller;

import com.dneydev.fluxia.domain.Categoria;
import com.dneydev.fluxia.dto.CategoriaRequest;
import com.dneydev.fluxia.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    public List<Categoria> listar() {
        return categoriaService.listarTodas();
    }

    @PostMapping
    public ResponseEntity<Categoria> criar(@Valid @RequestBody CategoriaRequest request) {
        Categoria categoria = categoriaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoria);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        categoriaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}