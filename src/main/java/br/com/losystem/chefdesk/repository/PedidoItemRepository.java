package br.com.losystem.chefdesk.repository;

import br.com.losystem.chefdesk.domain.entity.PedidoItem;
import br.com.losystem.chefdesk.domain.enums.StatusItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoItemRepository extends JpaRepository<PedidoItem,Long> {

    List<PedidoItem> findByPedidoId(Long pedidoId);

    List<PedidoItem> findByStatusOrderByIdAsc(StatusItemPedido status);

}
