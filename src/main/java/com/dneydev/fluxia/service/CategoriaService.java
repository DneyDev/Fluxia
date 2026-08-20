package com.dneydev.fluxia.service;

import com.dneydev.fluxia.domain.Categoria;
import com.dneydev.fluxia.dto.CategoriaRequest;
import com.dneydev.fluxia.exception.RecursoNaoEncontradoException;
import com.dneydev.fluxia.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    public Categoria criar(CategoriaRequest request) {
        Categoria categoria = Categoria.builder()
                .nome(request.nome())
                .tipo(request.tipo())
                .build();
        return categoriaRepository.save(categoria);
    }

    public Categoria buscarOuCriarPorNome(String nome, com.dneydev.fluxia.domain.TipoTransacao tipoPadrao) {
        return categoriaRepository.findByNomeIgnoreCase(nome)
                .orElseGet(() -> categoriaRepository.save(
                        Categoria.builder().nome(nome).tipo(tipoPadrao).build()
                ));
    }

    public void deletar(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Categoria não encontrada: " + id);
        }
        categoriaRepository.deleteById(id);
    }
}