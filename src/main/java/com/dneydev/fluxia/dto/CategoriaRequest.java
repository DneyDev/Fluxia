package com.dneydev.fluxia.dto;

import com.dneydev.fluxia.domain.TipoTransacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoriaRequest(
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotNull(message = "O tipo é obrigatório")
        TipoTransacao tipo
) {}