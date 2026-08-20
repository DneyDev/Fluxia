package com.dneydev.fluxia.service;

import com.dneydev.fluxia.dto.ComandoInterpretado;
import com.dneydev.fluxia.dto.TransacaoRequest;
import com.dneydev.fluxia.dto.TransacaoResponse;
import com.dneydev.fluxia.service.ia.AssistenteIA;
import com.dneydev.fluxia.service.ia.GeradorVoz;
import com.dneydev.fluxia.service.ia.TranscritorAudio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AssistenteService {

    private final AssistenteIA assistenteIA;
    private final TranscritorAudio transcritorAudio;
    private final GeradorVoz geradorVoz;
    private final TransacaoService transacaoService;

    public String processarComando(String textoComando) {
        return interpretarEExecutar(textoComando);
    }

    public String processarAudio(MultipartFile arquivoAudio) {
        String textoTranscrito = transcritorAudio.transcrever(arquivoAudio);
        return interpretarEExecutar(textoTranscrito);
    }

    public byte[] processarAudioComRespostaEmVoz(MultipartFile arquivoAudio) {
        String respostaTexto = processarAudio(arquivoAudio);
        return geradorVoz.gerarAudio(respostaTexto);
    }

    private String interpretarEExecutar(String textoComando) {
        ComandoInterpretado comando = assistenteIA.interpretarComando(textoComando);

        return switch (comando.acao()) {
            case "CRIAR_TRANSACAO" -> {
                TransacaoResponse criada = TransacaoResponse.fromEntity(
                        transacaoService.criar(new TransacaoRequest(
                                comando.descricao(),
                                comando.valor(),
                                comando.tipo(),
                                comando.data(),
                                comando.categoriaNome()
                        ))
                );
                yield "Transação registrada: %s de R$ %.2f (%s)".formatted(
                        criada.descricao(), criada.valor(), criada.tipo());
            }
            case "CONSULTAR_SALDO" -> {
                LocalDate hoje = LocalDate.now();
                BigDecimal saldo = transacaoService.calcularSaldoPorPeriodo(
                        hoje.withDayOfMonth(1), hoje);
                yield "Seu saldo do mês atual é R$ %.2f".formatted(saldo);
            }
            default -> "Não consegui entender o comando. Pode reformular?";
        };
    }
}