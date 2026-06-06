package br.com.losystem.chefdesk.service;

import br.com.losystem.chefdesk.domain.entity.Mesa;
import br.com.losystem.chefdesk.domain.entity.Pedido;
import br.com.losystem.chefdesk.domain.entity.PedidoItem;
import br.com.losystem.chefdesk.domain.entity.Produto;
import br.com.losystem.chefdesk.domain.enums.StatusItemPedido;
import br.com.losystem.chefdesk.domain.enums.StatusMesa;
import br.com.losystem.chefdesk.domain.enums.StatusPedido;
import br.com.losystem.chefdesk.dto.request.PedidoItemRequest;
import br.com.losystem.chefdesk.dto.request.PedidoRequest;
import br.com.losystem.chefdesk.dto.response.PedidoItemResponse;
import br.com.losystem.chefdesk.dto.response.PedidoResponse;
import br.com.losystem.chefdesk.exception.RegraNegocioException;
import br.com.losystem.chefdesk.repository.MesaRepository;
import br.com.losystem.chefdesk.repository.PedidoItemRepository;
import br.com.losystem.chefdesk.repository.PedidoRepository;
import br.com.losystem.chefdesk.repository.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final MesaRepository mesaRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoItemRepository itemRepository;

    public PedidoService(PedidoRepository pedidoRepository, MesaRepository mesaRepository,
                         ProdutoRepository produtoRepository, PedidoItemRepository itemRepository) {
        this.pedidoRepository = pedidoRepository;
        this.mesaRepository = mesaRepository;
        this.produtoRepository = produtoRepository;
        this.itemRepository = itemRepository;
    }

    public PedidoResponse abrirPedido(PedidoRequest pedidoRequest) {
        Mesa mesa = mesaRepository.findById(pedidoRequest.mesaId())
                .orElseThrow(() -> new RuntimeException("Mesa inexistente"));

        if (mesa.getStatus() != StatusMesa.LIVRE) {
            throw new RuntimeException("Mesa não esta livre para abertura de pedido");
        }

        Pedido pedido = new Pedido();
        pedido.setMesa(mesa);
        pedido.setStatus(StatusPedido.ABERTO);
        pedido.setObservacao(pedidoRequest.observacao());

        mesa.setStatus(StatusMesa.OCUPADA);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        mesaRepository.save(mesa);

        return PedidoResponse.fromEntity(pedidoSalvo);
    }

    public Page<PedidoResponse> listar(Pageable pageable) {
        return pedidoRepository.findAll(pageable).map(PedidoResponse::fromEntity);
    }

    public PedidoResponse buscarPorId(Long id) {
        Pedido pedido = buscarPedidoPorId(id);
        return PedidoResponse.fromEntity(pedido);
    }

    public PedidoItemResponse adicionarItem(Long pedidoId, PedidoItemRequest request) {
        Pedido pedido = buscarPedidoPorId(pedidoId);
        if (pedido.getStatus() != StatusPedido.ABERTO) {
            throw new RegraNegocioException("Só é possivel adicionar item em pedidos aberto");
        }

        Produto produto = produtoRepository.findById(request.produtoId()).orElseThrow(
                () -> new RegraNegocioException("Produto inexistente")
        );

        if (!produto.getDisponivel()) {
            throw new RegraNegocioException("Produto indisponivel no cerdapio");
        }

        if (request.quantidade() == null || request.quantidade() <= 0) {
            throw new RegraNegocioException("A quantidade deve ser maior que 0");
        }

        PedidoItem pedidoItem = new PedidoItem();
        pedidoItem.setPedido(pedido);
        pedidoItem.setProduto(produto);
        pedidoItem.setQuantidade(request.quantidade());
        pedidoItem.setPrecoUnitario(produto.getPreco());
        pedidoItem.setObservacao(request.observacao());
        pedidoItem.setStatus(StatusItemPedido.PENDENTE);
        PedidoItem itemSalvo = itemRepository.save(pedidoItem);
        return PedidoItemResponse.fromEntity(itemSalvo);
    }

    public List<PedidoItemResponse> listarItens(Long pedidoId) {
        buscarPedidoPorId(pedidoId);

        return itemRepository.findByPedidoId(pedidoId)
                .stream().map(PedidoItemResponse::fromEntity).collect(Collectors.toList());
    }

    private Pedido buscarPedidoPorId(Long pedidoId) {
        return pedidoRepository.findById(pedidoId).orElseThrow(
                () -> new RegraNegocioException("Pedido não encontrado")
        );
    }
}
