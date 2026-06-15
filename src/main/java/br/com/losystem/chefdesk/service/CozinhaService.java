package br.com.losystem.chefdesk.service;

import br.com.losystem.chefdesk.domain.entity.PedidoItem;
import br.com.losystem.chefdesk.domain.enums.StatusItemPedido;
import br.com.losystem.chefdesk.dto.response.CozinhaItemResponse;
import br.com.losystem.chefdesk.exception.RegraNegocioException;
import br.com.losystem.chefdesk.repository.PedidoItemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CozinhaService {

    private final PedidoItemRepository itemRepository;

    public CozinhaService(PedidoItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<CozinhaItemResponse> listarItemPedentes() {
        return itemRepository.findByStatusOrderByIdAsc(StatusItemPedido.PENDENTE)
                .stream().map(CozinhaItemResponse::fromEntity).toList();
    }

    public List<CozinhaItemResponse> listarItemEmPreparo() {
        return itemRepository.findByStatusOrderByIdAsc(StatusItemPedido.EM_PREPARO)
                .stream().map(CozinhaItemResponse::fromEntity).toList();
    }

    public CozinhaItemResponse iniciarPreparo(Long itemId) {
        PedidoItem pedidoItem = buscarItemPorId(itemId);
        if (pedidoItem.getStatus() != StatusItemPedido.PENDENTE) {
            throw new RegraNegocioException("Somente itens pendentes podem iniciar preparo");
        }
        pedidoItem.setStatus(StatusItemPedido.EM_PREPARO);
        pedidoItem.setDataInicioPreparo(LocalDateTime.now());

        return CozinhaItemResponse.fromEntity(itemRepository.save(pedidoItem));
    }

    public CozinhaItemResponse marcarComoPronto(Long itemId) {
        PedidoItem pedidoItem = buscarItemPorId(itemId);

        if (pedidoItem.getStatus() != StatusItemPedido.EM_PREPARO) {
            throw new RegraNegocioException("Somente itens em preparo podem ser marcados como pronto");
        }
        pedidoItem.setStatus(StatusItemPedido.PRONTO);
        pedidoItem.setDataPronto(LocalDateTime.now());

        return CozinhaItemResponse.fromEntity(itemRepository.save(pedidoItem));
    }

    public CozinhaItemResponse entregarPedido(Long itemId) {
        PedidoItem pedidoItem = buscarItemPorId(itemId);

        if (pedidoItem.getStatus() != StatusItemPedido.PRONTO) {
            throw new RegraNegocioException("Somente itens prontos  podem ser entregues");
        }
        pedidoItem.setStatus(StatusItemPedido.ENTREGUE);
        pedidoItem.setDataEntrega(LocalDateTime.now());

        return CozinhaItemResponse.fromEntity(itemRepository.save(pedidoItem));
    }

    private PedidoItem buscarItemPorId(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(
                () -> new RegraNegocioException("Item não encontrado")
        );
    }
}
