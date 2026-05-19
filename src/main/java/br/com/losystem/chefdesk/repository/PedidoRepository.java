package br.com.losystem.chefdesk.repository;

import br.com.losystem.chefdesk.domain.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido,Long> {
}
