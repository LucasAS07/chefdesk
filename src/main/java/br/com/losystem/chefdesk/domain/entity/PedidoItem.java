package br.com.losystem.chefdesk.domain.entity;

import br.com.losystem.chefdesk.domain.enums.StatusItemPedido;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pedido_item")
public class PedidoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantidae;

    @Column(name = "preco_unitario")
    private BigDecimal precoUnitario;

    private String observacao;

    @Enumerated(EnumType.STRING)
    private StatusItemPedido status = StatusItemPedido.PENDENTE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id")
    private Produto produto;
}
