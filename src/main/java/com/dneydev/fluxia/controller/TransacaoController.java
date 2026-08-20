package com.dneydev.fluxia.controller;

import com.dneydev.fluxia.domain.Transacao;
import com.dneydev.fluxia.dto.TransacaoRequest;
import com.dneydev.fluxia.dto.TransacaoResponse;
import com.dneydev.fluxia.service.TransacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transacoes")
@RequiredArgsConstructor
public class TransacaoController {

    private final TransacaoService transacaoService;

    @GetMapping
    public List<TransacaoResponse> listar() {
        return transacaoService.listarTodas().stream()
                .map(TransacaoResponse::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public TransacaoResponse buscarPorId(@PathVariable Long id) {
        return TransacaoResponse.fromEntity(transacaoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<TransacaoResponse> criar(@Valid @RequestBody TransacaoRequest request) {
        Transacao criada = transacaoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(TransacaoResponse.fromEntity(criada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        transacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/saldo")
    public BigDecimal saldoPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return transacaoService.calcularSaldoPorPeriodo(inicio, fim);
    }
}