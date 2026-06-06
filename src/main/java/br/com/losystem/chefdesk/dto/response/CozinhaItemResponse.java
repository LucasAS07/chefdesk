package br.com.losystem.chefdesk.dto.response;

import br.com.losystem.chefdesk.domain.entity.PedidoItem;
import br.com.losystem.chefdesk.domain.enums.StatusItemPedido;

import java.math.BigDecimal;

public record CozinhaItemResponse(
        Long id,
        Long pedidoId,
        Integer numeroMesa,
        String produtoNome,
        Integer quantidade,
        String observacao,
        BigDecimal precoUnitario,
        StatusItemPedido status
) {
    public static CozinhaItemResponse fromEntity(PedidoItem item) {
        return new CozinhaItemResponse(
                item.getId(),
                item.getPedido().getId(),
                item.getPedido().getMesa().getNumero(),
                item.getProduto().getNome(),
                item.getQuantidade(),
                item.getObservacao(),
                item.getPrecoUnitario(),
                item.getStatus()
        );
    }
}
