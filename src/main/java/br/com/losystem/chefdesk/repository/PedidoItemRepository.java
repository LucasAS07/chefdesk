package br.com.losystem.chefdesk.repository;

import br.com.losystem.chefdesk.domain.entity.PedidoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoItemRepository extends JpaRepository<PedidoItem,Long> {
}
