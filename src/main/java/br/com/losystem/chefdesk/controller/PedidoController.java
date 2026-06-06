package br.com.losystem.chefdesk.controller;

import br.com.losystem.chefdesk.dto.request.PedidoItemRequest;
import br.com.losystem.chefdesk.dto.request.PedidoRequest;
import br.com.losystem.chefdesk.dto.response.PedidoItemResponse;
import br.com.losystem.chefdesk.dto.response.PedidoResponse;
import br.com.losystem.chefdesk.service.PedidoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponse abrirPedido(@RequestBody PedidoRequest pedidoRequest) {
        return pedidoService.abrirPedido(pedidoRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Page<PedidoResponse> listar(Pageable pageable) {
        return pedidoService.listar(pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PedidoResponse buscarPorId(@PathVariable Long id) {
        return pedidoService.buscarPorId(id);
    }

    @PostMapping("/{pedidoId}/itens")
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoItemResponse adicionarIten(@PathVariable Long pedidoId, @RequestBody PedidoItemRequest request) {
        return pedidoService.adicionarItem(pedidoId,request);
    }

    @GetMapping("/{pedidoId}/itens")
    @ResponseStatus(HttpStatus.OK)
    public List<PedidoItemResponse> listarItensDoPedido(@PathVariable Long pedidoId) {
        return pedidoService.listarItens(pedidoId);
    }
}
