package com.dneydev.fluxia.service.ia;

public interface GeradorVoz {
    byte[] gerarAudio(String texto);
}