package br.com.losystem.chefdesk.dto;

import br.com.losystem.chefdesk.domain.entity.Produto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProdutoResponse(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        Boolean disponivel,
        Integer tempoDePreparoMinutos,
        Long categoriaId,
        String categoriaNome,
        LocalDateTime criadoEm
) {

    public static ProdutoResponse fromEntity(Produto produto) {
        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getDisponivel(),
                produto.getTempoPreparoMinutos(),
                produto.getCategoria().getId(),
                produto.getCategoria().getNome(),
                produto.getCriadoEm()
        );
    }

}
