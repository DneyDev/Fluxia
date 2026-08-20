package com.dneydev.fluxia.service.ia;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Implementação sem IA real: simula a transcrição assumindo que o "áudio"
 * enviado nos testes já é um texto puro (facilita testar o fluxo completo
 * sem depender de Whisper/API paga). Ativa por padrão (perfil "ai" desligado).
 */
@Service
@Profile("!ai")
public class TranscritorAudioStub implements TranscritorAudio {

    @Override
    public String transcrever(MultipartFile arquivoAudio) {
        if (arquivoAudio == null || arquivoAudio.isEmpty()) {
            throw new IllegalArgumentException("Arquivo de áudio vazio ou não enviado");
        }

        // Simulação: o "conteúdo do áudio" é lido como texto puro.
        // Em produção (perfil "ai"), aqui entraria a chamada ao Whisper.
        try {
            return new String(arquivoAudio.getBytes()).trim();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar arquivo de áudio", e);
        }
    }
}