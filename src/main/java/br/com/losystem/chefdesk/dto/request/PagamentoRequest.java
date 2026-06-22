package br.com.losystem.chefdesk.dto.request;

import java.math.BigDecimal;

public record PagamentoRequest(
        BigDecimal valor,
        String formaPagamento
) {
}
