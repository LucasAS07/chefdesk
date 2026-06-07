package br.com.losystem.chefdesk.controller;

import br.com.losystem.chefdesk.dto.request.FechamentoContaRequest;
import br.com.losystem.chefdesk.dto.response.FechamentoContaResponse;
import br.com.losystem.chefdesk.service.FechamentoContaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos/{pedidoId}/fechamento")
public class FechamentoContaController {

    private final FechamentoContaService contaService;

    public FechamentoContaController(FechamentoContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FechamentoContaResponse fecharConta(@PathVariable Long pedidoId,
                                               @RequestBody FechamentoContaRequest contaRequest) {
        return contaService.fecharConta(pedidoId,contaRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public FechamentoContaResponse fecharConta(@PathVariable Long pedidoId) {
        return contaService.buscarPorPedido(pedidoId);
    }
}
