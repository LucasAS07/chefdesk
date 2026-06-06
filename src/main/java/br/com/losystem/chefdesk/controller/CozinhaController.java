package br.com.losystem.chefdesk.controller;

import br.com.losystem.chefdesk.dto.response.CozinhaItemResponse;
import br.com.losystem.chefdesk.service.CozinhaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cozinha")
public class CozinhaController {

    private final CozinhaService cozinhaService;

    public CozinhaController(CozinhaService cozinhaService) {
        this.cozinhaService = cozinhaService;
    }

    @GetMapping("/itens-pedentes")
    @ResponseStatus(HttpStatus.OK)
    public List<CozinhaItemResponse> listarPendentes() {
        return cozinhaService.listarItemPedentes();
    }

    @GetMapping("/itens-em-preparo")
    public List<CozinhaItemResponse> listarItensEmPreparo() {
        return cozinhaService.listarItemEmPreparo();
    }

    @PatchMapping("/itens/{itemId}/iniciar-preparo")
    public CozinhaItemResponse iniciarPreparo(@PathVariable Long itemId) {
        return cozinhaService.iniciarPreparo(itemId);
    }

    @PatchMapping("/itens/{itemId}/marcar-pronto")
    public CozinhaItemResponse marcarComoPronto(@PathVariable Long itemId) {
        return cozinhaService.marcarComoPronto(itemId);
    }

    @PatchMapping("/itens/{itemId}/entregar")
    public CozinhaItemResponse entregar(@PathVariable Long itemId) {
        return cozinhaService.entregarPedido(itemId);
    }

}
