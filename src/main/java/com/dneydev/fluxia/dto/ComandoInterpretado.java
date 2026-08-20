package com.dneydev.fluxia.dto;

import com.dneydev.fluxia.domain.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ComandoInterpretado(
        String acao,          // "CRIAR_TRANSACAO" | "CONSULTAR_SALDO" | "DESCONHECIDO"
        String descricao,
        BigDecimal valor,
        TipoTransacao tipo,
        String categoriaNome,
        LocalDate data
) {}