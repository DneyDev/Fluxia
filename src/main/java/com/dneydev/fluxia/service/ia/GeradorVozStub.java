package com.dneydev.fluxia.service.ia;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * Implementação sem IA real: não gera áudio de fato, apenas retorna o texto
 * da resposta como bytes, simulando o retorno de um endpoint de áudio.
 * Permite testar o fluxo completo (comando -> ação -> "áudio" de resposta)
 * sem depender de uma API de TTS paga. Em produção (perfil "ai"), aqui
 * entraria a chamada real ao serviço de text-to-speech.
 */
@Service
@Profile("!ai")
public class GeradorVozStub implements GeradorVoz {

    @Override
    public byte[] gerarAudio(String texto) {
        String pseudoAudio = "[AUDIO SIMULADO] " + texto;
        return pseudoAudio.getBytes(StandardCharsets.UTF_8);
    }
}