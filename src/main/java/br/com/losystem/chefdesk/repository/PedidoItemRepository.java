package br.com.losystem.chefdesk.repository;

import br.com.losystem.chefdesk.domain.entity.PedidoItem;
import br.com.losystem.chefdesk.domain.enums.StatusItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PedidoItemRepository extends JpaRepository<PedidoItem,Long> {

    List<PedidoItem> findByPedidoId(Long pedidoId);

    List<PedidoItem> findByStatusOrderByIdAsc(StatusItemPedido status);

    List<PedidoItem> findByPedidoIdAndStatusNot(Long PedidoId, StatusItemPedido status);

    @Query("""
        select i
        from PedidoItem i
        join fetch i.produto
                join fetch i.pedido p
                        join fetch p.mesa
                                where i.status = :status
                                        order by i.id
                         
        """)
    List<PedidoItem> buscarItensComProdutoEPedido(StatusItemPedido status);

}
