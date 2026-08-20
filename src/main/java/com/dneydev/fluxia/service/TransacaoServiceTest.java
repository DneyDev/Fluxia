package com.dneydev.fluxia.service;

import com.dneydev.fluxia.domain.Categoria;
import com.dneydev.fluxia.domain.Transacao;
import com.dneydev.fluxia.domain.TipoTransacao;
import com.dneydev.fluxia.dto.TransacaoRequest;
import com.dneydev.fluxia.exception.RecursoNaoEncontradoException;
import com.dneydev.fluxia.repository.TransacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransacaoServiceTest {

    @Mock
    private TransacaoRepository transacaoRepository;

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private TransacaoService transacaoService;

    private Transacao transacaoExemplo;

    @BeforeEach
    void setUp() {
        transacaoExemplo = Transacao.builder()
                .id(1L)
                .descricao("Mercado")
                .valor(new BigDecimal("150.00"))
                .tipo(TipoTransacao.DESPESA)
                .data(LocalDate.of(2026, 8, 10))
                .build();
    }

    @Test
    void deveCriarTransacaoSemCategoria() {
        TransacaoRequest request = new TransacaoRequest(
                "Mercado", new BigDecimal("150.00"), TipoTransacao.DESPESA,
                LocalDate.of(2026, 8, 10), null);

        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacaoExemplo);

        Transacao resultado = transacaoService.criar(request);

        assertThat(resultado.getDescricao()).isEqualTo("Mercado");
        assertThat(resultado.getValor()).isEqualByComparingTo("150.00");
        verify(categoriaService, never()).buscarOuCriarPorNome(any(), any());
        verify(transacaoRepository).save(any(Transacao.class));
    }

    @Test
    void deveCriarTransacaoResolvendoCategoriaQuandoInformada() {
        TransacaoRequest request = new TransacaoRequest(
                "Mercado", new BigDecimal("150.00"), TipoTransacao.DESPESA,
                LocalDate.of(2026, 8, 10), "Alimentação");

        Categoria categoria = Categoria.builder().id(1L).nome("Alimentação").tipo(TipoTransacao.DESPESA).build();
        when(categoriaService.buscarOuCriarPorNome("Alimentação", TipoTransacao.DESPESA)).thenReturn(categoria);
        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacaoExemplo);

        transacaoService.criar(request);

        verify(categoriaService).buscarOuCriarPorNome("Alimentação", TipoTransacao.DESPESA);
    }

    @Test
    void deveLancarExcecaoAoBuscarTransacaoInexistente() {
        when(transacaoRepository.findById(99L)).thenReturn(java.util.Optional.empty());

        assertThatThrownBy(() -> transacaoService.buscarPorId(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("99");
    }

    @Test
    void deveCalcularSaldoComoReceitasMenosDespesas() {
        when(transacaoRepository.somarPorTipoEPeriodo(eq(TipoTransacao.RECEITA), any(), any()))
                .thenReturn(new BigDecimal("1000.00"));
        when(transacaoRepository.somarPorTipoEPeriodo(eq(TipoTransacao.DESPESA), any(), any()))
                .thenReturn(new BigDecimal("350.00"));

        BigDecimal saldo = transacaoService.calcularSaldoPorPeriodo(
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 31));

        assertThat(saldo).isEqualByComparingTo("650.00");
    }

    @Test
    void deveLancarExcecaoAoDeletarTransacaoInexistente() {
        when(transacaoRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> transacaoService.deletar(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class);

        verify(transacaoRepository, never()).deleteById(any());
    }
}