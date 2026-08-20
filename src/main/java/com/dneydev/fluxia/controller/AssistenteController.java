package com.dneydev.fluxia.controller;

import com.dneydev.fluxia.dto.ComandoRequest;
import com.dneydev.fluxia.service.AssistenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assistente")
@RequiredArgsConstructor
public class AssistenteController {

    private final AssistenteService assistenteService;

    @PostMapping("/comando")
    public String processarComando(@Valid @RequestBody ComandoRequest request) {
        return assistenteService.processarComando(request.comando());
    }
}