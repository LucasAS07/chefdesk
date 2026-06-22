package br.com.losystem.chefdesk.dto.response;

public record PagamentoResponse(
        String status,
        String codigoTransacao
) {
}
