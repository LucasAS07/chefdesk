package br.com.losystem.chefdesk.dto.response;

import br.com.losystem.chefdesk.domain.entity.FechamentoConta;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FechamentoContaResponse(
        Long id,
        Long pedidoId,
        Integer numeroMesa,
        BigDecimal subtotal,
        BigDecimal taxaServico,
        BigDecimal desconto,
        BigDecimal total,
        LocalDate dataFechamento
) {
    public static FechamentoContaResponse fromEntiy(FechamentoConta fechamento) {
        return new FechamentoContaResponse(
                fechamento.getId(),
                fechamento.getPedido().getId(),
                fechamento.getPedido().getMesa().getNumero(),
                fechamento.getSubtotal(),
                fechamento.getTaxaServico(),
                fechamento.getDesconto(),
                fechamento.getTotal(),
                fechamento.getDataFechamento()
        );
    }
}
