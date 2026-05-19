package br.com.losystem.chefdesk.repository;

import br.com.losystem.chefdesk.domain.entity.CategoriaProduto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaProdutoRepository extends JpaRepository<CategoriaProduto,Long> {
}
