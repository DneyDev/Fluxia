package com.dneydev.fluxia.service.ia;

import com.dneydev.fluxia.dto.ComandoInterpretado;

public interface AssistenteIA {
    ComandoInterpretado interpretarComando(String textoComando);
}