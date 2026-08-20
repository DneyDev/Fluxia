package com.dneydev.fluxia.service.ia;

import com.dneydev.fluxia.domain.TipoTransacao;
import com.dneydev.fluxia.dto.ComandoInterpretado;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class AssistenteIAStubTest {

    private final AssistenteIAStub stub = new AssistenteIAStub();

    @Test
    void deveInterpretarComandoDeGasto() {
        ComandoInterpretado resultado = stub.interpretarComando("gastei 50 reais em mercado");

        assertThat(resultado.acao()).isEqualTo("CRIAR_TRANSACAO");
        assertThat(resultado.tipo()).isEqualTo(TipoTransacao.DESPESA);
        assertThat(resultado.valor()).isEqualByComparingTo(new BigDecimal("50"));
        assertThat(resultado.categoriaNome()).isEqualTo("mercado");
    }

    @Test
    void deveInterpretarComandoDeRecebimento() {
        ComandoInterpretado resultado = stub.interpretarComando("recebi 2000 reais de salário");

        assertThat(resultado.acao()).isEqualTo("CRIAR_TRANSACAO");
        assertThat(resultado.tipo()).isEqualTo(TipoTransacao.RECEITA);
        assertThat(resultado.valor()).isEqualByComparingTo(new BigDecimal("2000"));
    }

    @Test
    void deveInterpretarComandoDeConsultaDeSaldo() {
        ComandoInterpretado resultado = stub.interpretarComando("qual o meu saldo?");

        assertThat(resultado.acao()).isEqualTo("CONSULTAR_SALDO");
    }

    @Test
    void deveRetornarDesconhecidoParaComandoNaoMapeado() {
        ComandoInterpretado resultado = stub.interpretarComando("me conta uma piada");

        assertThat(resultado.acao()).isEqualTo("DESCONHECIDO");
    }

    @Test
    void deveAceitarValorComVirgulaComoDecimal() {
        ComandoInterpretado resultado = stub.interpretarComando("gastei 45,90 reais em farmácia");

        assertThat(resultado.valor()).isEqualByComparingTo(new BigDecimal("45.90"));
    }
}