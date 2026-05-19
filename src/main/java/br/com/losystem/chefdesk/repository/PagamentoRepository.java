package br.com.losystem.chefdesk.repository;

import br.com.losystem.chefdesk.domain.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento,Long> {
}
