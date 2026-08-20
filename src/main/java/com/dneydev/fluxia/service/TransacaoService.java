package com.dneydev.fluxia.service;

import com.dneydev.fluxia.domain.Categoria;
import com.dneydev.fluxia.domain.Transacao;
import com.dneydev.fluxia.dto.TransacaoRequest;
import com.dneydev.fluxia.exception.RecursoNaoEncontradoException;
import com.dneydev.fluxia.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final CategoriaService categoriaService;

    public List<Transacao> listarTodas() {
        return transacaoRepository.findAll();
    }

    public Transacao buscarPorId(Long id) {
        return transacaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Transação não encontrada: " + id));
    }

    public Transacao criar(TransacaoRequest request) {
        Categoria categoria = null;
        if (request.categoriaNome() != null && !request.categoriaNome().isBlank()) {
            categoria = categoriaService.buscarOuCriarPorNome(request.categoriaNome(), request.tipo());
        }

        Transacao transacao = Transacao.builder()
                .descricao(request.descricao())
                .valor(request.valor())
                .tipo(request.tipo())
                .data(request.data())
                .categoria(categoria)
                .build();

        return transacaoRepository.save(transacao);
    }

    public void deletar(Long id) {
        if (!transacaoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Transação não encontrada: " + id);
        }
        transacaoRepository.deleteById(id);
    }

    public BigDecimal calcularSaldoPorPeriodo(LocalDate inicio, LocalDate fim) {
        BigDecimal receitas = transacaoRepository.somarPorTipoEPeriodo(
                com.dneydev.fluxia.domain.TipoTransacao.RECEITA, inicio, fim);
        BigDecimal despesas = transacaoRepository.somarPorTipoEPeriodo(
                com.dneydev.fluxia.domain.TipoTransacao.DESPESA, inicio, fim);
        return receitas.subtract(despesas);
    }
}