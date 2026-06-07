package br.com.losystem.chefdesk.repository;

import br.com.losystem.chefdesk.domain.entity.FechamentoConta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FechamentoContaRepository extends JpaRepository<FechamentoConta,Long> {

    boolean existsByPedidoId(Long pedidoId);

    Optional<FechamentoConta> findByPedidoId(Long pedidoId);

}
