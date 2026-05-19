package br.com.losystem.chefdesk.repository;

import br.com.losystem.chefdesk.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto,Long> {
}
