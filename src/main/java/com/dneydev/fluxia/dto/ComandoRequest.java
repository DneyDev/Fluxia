package com.dneydev.fluxia.dto;

import jakarta.validation.constraints.NotBlank;

public record ComandoRequest(
        @NotBlank(message = "O comando é obrigatório")
        String comando
) {}