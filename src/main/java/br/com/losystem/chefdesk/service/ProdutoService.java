package br.com.losystem.chefdesk.service;

import br.com.losystem.chefdesk.domain.entity.CategoriaProduto;
import br.com.losystem.chefdesk.domain.entity.Produto;
import br.com.losystem.chefdesk.dto.request.ProdutoRequest;
import br.com.losystem.chefdesk.dto.response.ProdutoResponse;
import br.com.losystem.chefdesk.repository.CategoriaProdutoRepository;
import br.com.losystem.chefdesk.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaProdutoRepository categoriaRepository;

    public ProdutoResponse cadastrar(ProdutoRequest request) {
        CategoriaProduto categoriaProduto = buscarCategoriaPorId(request);
        Produto produto = request.toEntity(categoriaProduto);
        Produto produtoSalvo = produtoRepository.save(produto);
        return ProdutoResponse.fromEntity(produtoSalvo);
    }

    public Page<ProdutoResponse> listar(Pageable pageable) {
        return produtoRepository.findAll(pageable).map(ProdutoResponse::fromEntity);
    }

    public ProdutoResponse buscarPorId(Long id) {
        Produto produto = buscarProdutoPorId(id);
        return ProdutoResponse.fromEntity(produto);
    }

    public ProdutoResponse atualizar(Long id, ProdutoRequest request) {
        Produto produto = buscarProdutoPorId(id);
        CategoriaProduto categoriaProduto = buscarCategoriaPorId(request);

        request.preencher(produto, categoriaProduto);
        Produto produtoAtualizado = produtoRepository.save(produto);
        return ProdutoResponse.fromEntity(produtoAtualizado);
    }

    public void excluir(Long id) {
        Produto produto = buscarProdutoPorId(id);
        produtoRepository.delete(produto);
    }

    private Produto buscarProdutoPorId(Long id) {
        return produtoRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Produto não encontrado")
        );
    }

    private CategoriaProduto buscarCategoriaPorId(ProdutoRequest request) {
        return categoriaRepository.findById(request.categoriaId()).orElseThrow(
                () -> new RuntimeException("Categoria não encontrada")
        );
    }
}
