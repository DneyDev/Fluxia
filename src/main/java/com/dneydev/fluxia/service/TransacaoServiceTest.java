package com.dneydev.fluxia.service;

import com.dneydev.fluxia.domain.Transacao;
import com.dneydev.fluxia.domain.TipoTransacao;
import com.dneydev.fluxia.dto.ComandoInterpretado;
import com.dneydev.fluxia.service.ia.AssistenteIA;
import com.dneydev.fluxia.service.ia.GeradorVoz;
import com.dneydev.fluxia.service.ia.TranscritorAudio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssistenteServiceTest {

    @Mock private AssistenteIA assistenteIA;
    @Mock private TranscritorAudio transcritorAudio;
    @Mock private GeradorVoz geradorVoz;
    @Mock private TransacaoService transacaoService;

    @InjectMocks
    private AssistenteService assistenteService;

    @Test
    void deveRegistrarTransacaoQuandoComandoForCriarTransacao() {
        ComandoInterpretado comando = new ComandoInterpretado(
                "CRIAR_TRANSACAO", "Gasto com mercado", new BigDecimal("50.00"),
                TipoTransacao.DESPESA, "mercado", LocalDate.now());

        when(assistenteIA.interpretarComando("gastei 50 reais em mercado")).thenReturn(comando);
        when(transacaoService.criar(any())).thenReturn(
                Transacao.builder()
                        .descricao("Gasto com mercado")
                        .valor(new BigDecimal("50.00"))
                        .tipo(TipoTransacao.DESPESA)
                        .data(LocalDate.now())
                        .build());

        String resposta = assistenteService.processarComando("gastei 50 reais em mercado");

        assertThat(resposta).contains("Transação registrada").contains("50");
    }

    @Test
    void deveConsultarSaldoQuandoComandoForConsultarSaldo() {
        ComandoInterpretado comando = new ComandoInterpretado(
                "CONSULTAR_SALDO", null, null, null, null, null);

        when(assistenteIA.interpretarComando("qual meu saldo")).thenReturn(comando);
        when(transacaoService.calcularSaldoPorPeriodo(any(), any())).thenReturn(new BigDecimal("650.00"));

        String resposta = assistenteService.processarComando("qual meu saldo");

        assertThat(resposta).contains("650");
    }

    @Test
    void deveRetornarMensagemPadraoParaComandoDesconhecido() {
        when(assistenteIA.interpretarComando("oi tudo bem"))
                .thenReturn(new ComandoInterpretado("DESCONHECIDO", null, null, null, null, null));

        String resposta = assistenteService.processarComando("oi tudo bem");

        assertThat(resposta).contains("Não consegui entender");
    }

    @Test
    void deveTranscreverAudioAntesDeInterpretar() {
        MockMultipartFile arquivo = new MockMultipartFile(
                "arquivo", "comando.txt", "text/plain", "qual meu saldo".getBytes());

        when(transcritorAudio.transcrever(arquivo)).thenReturn("qual meu saldo");
        when(assistenteIA.interpretarComando("qual meu saldo"))
                .thenReturn(new ComandoInterpretado("CONSULTAR_SALDO", null, null, null, null, null));
        when(transacaoService.calcularSaldoPorPeriodo(any(), any())).thenReturn(BigDecimal.ZERO);

        assistenteService.processarAudio(arquivo);

        // Confirma que o pipeline usou o texto transcrito, não o arquivo bruto
        org.mockito.Mockito.verify(assistenteIA).interpretarComando("qual meu saldo");
    }
}