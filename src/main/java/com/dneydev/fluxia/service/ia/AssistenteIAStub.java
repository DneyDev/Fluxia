package com.dneydev.fluxia.service.ia;

import com.dneydev.fluxia.domain.TipoTransacao;
import com.dneydev.fluxia.dto.ComandoInterpretado;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementação "burra" (sem IA real) que simula a interpretação de comandos
 * usando regras simples. Serve pra desenvolver e testar todo o fluxo da API
 * sem depender de uma chave de API paga. Ativa por padrão (perfil "ai" desligado).
 */
@Service
@Profile("!ai")
public class AssistenteIAStub implements AssistenteIA {

    private static final Pattern PADRAO_GASTO =
            Pattern.compile("gastei\\s+(\\d+(?:[.,]\\d{1,2})?)\\s+(?:reais\\s+)?em\\s+(.+)", Pattern.CASE_INSENSITIVE);

    private static final Pattern PADRAO_RECEBI =
            Pattern.compile("recebi\\s+(\\d+(?:[.,]\\d{1,2})?)\\s+(?:reais\\s+)?(?:de\\s+)?(.+)", Pattern.CASE_INSENSITIVE);

    @Override
    public ComandoInterpretado interpretarComando(String textoComando) {
        Matcher gasto = PADRAO_GASTO.matcher(textoComando);
        if (gasto.matches()) {
            return new ComandoInterpretado(
                    "CRIAR_TRANSACAO",
                    "Gasto com " + gasto.group(2).trim(),
                    new BigDecimal(gasto.group(1).replace(",", ".")),
                    TipoTransacao.DESPESA,
                    gasto.group(2).trim(),
                    LocalDate.now()
            );
        }

        Matcher recebi = PADRAO_RECEBI.matcher(textoComando);
        if (recebi.matches()) {
            return new ComandoInterpretado(
                    "CRIAR_TRANSACAO",
                    "Recebimento: " + recebi.group(2).trim(),
                    new BigDecimal(recebi.group(1).replace(",", ".")),
                    TipoTransacao.RECEITA,
                    recebi.group(2).trim(),
                    LocalDate.now()
            );
        }

        if (textoComando.toLowerCase().contains("saldo")) {
            return new ComandoInterpretado("CONSULTAR_SALDO", null, null, null, null, null);
        }

        return new ComandoInterpretado("DESCONHECIDO", null, null, null, null, null);
    }
}