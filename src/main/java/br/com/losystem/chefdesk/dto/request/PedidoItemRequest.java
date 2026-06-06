package br.com.losystem.chefdesk.dto.request;

public record PedidoItemRequest(
        Long produtoId,
        Integer quantidade,
        String observacao
) {
}
