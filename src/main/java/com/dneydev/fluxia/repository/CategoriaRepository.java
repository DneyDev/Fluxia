package com.dneydev.fluxia.repository;

import com.dneydev.fluxia.domain.Categoria;
import com.dneydev.fluxia.domain.TipoTransacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNomeIgnoreCase(String nome);

    List<Categoria> findByTipo(TipoTransacao tipo);
}