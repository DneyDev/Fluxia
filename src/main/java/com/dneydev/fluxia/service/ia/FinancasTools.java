package com.dneydev.fluxia.service.ia;

import com.dneydev.fluxia.domain.TipoTransacao;
import com.dneydev.fluxia.dto.TransacaoRequest;
import com.dneydev.fluxia.dto.TransacaoResponse;
import com.dneydev.fluxia.service.TransacaoService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Conjunto de "ferramentas" (functions) que a IA pode executar de verdade,
 * via Tool Calling. Cada @Tool vira uma função que o modelo de linguagem
 * decide chamar sozinho, com os parâmetros extraídos do comando do usuário.
 */
@Component
public class FinancasTools {

    private final TransacaoService transacaoService;

    public FinancasTools(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @Tool(description = "Registra uma nova transação financeira (receita ou despesa)")
    public String registrarTransacao(
            @ToolParam(description = "Descrição curta da transação") String descricao,
            @ToolParam(description = "Valor em reais, sempre positivo") BigDecimal valor,
            @ToolParam(description = "RECEITA ou DESPESA") TipoTransacao tipo,
            @ToolParam(description = "Nome da categoria, ex: mercado, transporte, salário") String categoriaNome
    ) {
        TransacaoResponse criada = TransacaoResponse.fromEntity(
                transacaoService.criar(new TransacaoRequest(descricao, valor, tipo, LocalDate.now(), categoriaNome))
        );
        return "Transação registrada: %s de R$ %.2f (%s)".formatted(
                criada.descricao(), criada.valor(), criada.tipo());
    }

    @Tool(description = "Consulta o saldo (receitas menos despesas) do mês atual")
    public String consultarSaldoMesAtual() {
        LocalDate hoje = LocalDate.now();
        BigDecimal saldo = transacaoService.calcularSaldoPorPeriodo(hoje.withDayOfMonth(1), hoje);
        return "Saldo do mês atual: R$ %.2f".formatted(saldo);
    }
}