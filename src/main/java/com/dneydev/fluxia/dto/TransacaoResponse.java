package com.dneydev.fluxia.dto;

import com.dneydev.fluxia.domain.Transacao;
import com.dneydev.fluxia.domain.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoResponse(
        Long id,
        String descricao,
        BigDecimal valor,
        TipoTransacao tipo,
        LocalDate data,
        String categoriaNome
) {
    public static TransacaoResponse fromEntity(Transacao t) {
        return new TransacaoResponse(
                t.getId(),
                t.getDescricao(),
                t.getValor(),
                t.getTipo(),
                t.getData(),
                t.getCategoria() != null ? t.getCategoria().getNome() : null
        );
    }
}