package br.com.losystem.chefdesk.controller;

import br.com.losystem.chefdesk.dto.request.ProdutoRequest;
import br.com.losystem.chefdesk.dto.response.ProdutoResponse;
import br.com.losystem.chefdesk.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponse cadastra(@RequestBody ProdutoRequest produtoRequest) {
        return produtoService.cadastrar(produtoRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ProdutoResponse> listar(Pageable pageable) {
        return produtoService.listar(pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProdutoResponse bucarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProdutoResponse atualizar(@PathVariable Long id, @RequestBody ProdutoRequest request) {
        return produtoService.atualizar(id,request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        produtoService.excluir(id);
    }

}
