package br.com.losystem.chefdesk.service;

import br.com.losystem.chefdesk.domain.entity.FechamentoConta;
import br.com.losystem.chefdesk.domain.entity.Pedido;
import br.com.losystem.chefdesk.domain.entity.PedidoItem;
import br.com.losystem.chefdesk.domain.enums.StatusItemPedido;
import br.com.losystem.chefdesk.domain.enums.StatusPedido;
import br.com.losystem.chefdesk.dto.request.FechamentoContaRequest;
import br.com.losystem.chefdesk.dto.response.FechamentoContaResponse;
import br.com.losystem.chefdesk.exception.RegraNegocioException;
import br.com.losystem.chefdesk.repository.FechamentoContaRepository;
import br.com.losystem.chefdesk.repository.PedidoItemRepository;
import br.com.losystem.chefdesk.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FechamentoContaService {

    private final FechamentoContaRepository fechamentoRepository;
    private final PedidoItemRepository itemRepository;
    private final PedidoRepository pedidoRepository;

    public FechamentoContaService(FechamentoContaRepository fechamentoRepository, PedidoItemRepository itemRepository,
                                  PedidoRepository pedidoRepository) {
        this.fechamentoRepository = fechamentoRepository;
        this.itemRepository = itemRepository;
        this.pedidoRepository = pedidoRepository;
    }

    public FechamentoContaResponse fecharConta(Long pedidoId, FechamentoContaRequest contaRequest) {
        Pedido pedido = buscarPedidoPorId(pedidoId);
        if (pedido.getStatus() == StatusPedido.FECHADO) {
            throw new RegraNegocioException("Pedido já esta fechado");
        }

        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new RegraNegocioException("Pedido cancelado não pode ser fechado");
        }

        if (fechamentoRepository.existsByPedidoId(pedidoId)) {
            throw new RegraNegocioException("Já existe fechamento para este pedido");
        }

        List<PedidoItem> itens = itemRepository.findByPedidoId(pedidoId);
        if (itens.isEmpty()) {
            throw new RegraNegocioException("Não é possivel fechar conta de pedido sem itens");
        }

        List<PedidoItem> itensNaoEntregues =
                itemRepository.findByPedidoIdAndStatusNot(pedidoId, StatusItemPedido.ENTREGUE);
        if (!itensNaoEntregues.isEmpty()) {
            throw new RegraNegocioException("Todods os itens precisam estar entregues para fechar a conta");
        }

        BigDecimal subTotal = itens.stream()
                .map(item -> item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal taxaServico = contaRequest.taxaServico() != null ? contaRequest.taxaServico() : BigDecimal.ZERO;
        BigDecimal desconto = contaRequest.desconto() != null ? contaRequest.desconto() : BigDecimal.ZERO;

        if (taxaServico.compareTo(BigDecimal.ZERO) < 0) {
            throw new RegraNegocioException("A taxa de serviço não pode ser negativa");
        }

        if (desconto.compareTo(BigDecimal.ZERO) < 0) {
            throw new RegraNegocioException("O desconto não pode ser negativo");
        }

        BigDecimal total = subTotal.add(taxaServico.subtract(desconto));
        if (total.compareTo(BigDecimal.ZERO) < 0) {
            throw new RegraNegocioException("O total da conta não pode ser negativo");
        }

        FechamentoConta fechamentoConta = new FechamentoConta();
        fechamentoConta.setPedido(pedido);
        fechamentoConta.setSubtotal(subTotal);
        fechamentoConta.setTaxaServico(taxaServico);
        fechamentoConta.setDesconto(desconto);
        fechamentoConta.setTotal(total);

        pedido.setStatus(StatusPedido.FECHADO);
        pedido.setDataFechamento(LocalDateTime.now());

        FechamentoConta fechamento = fechamentoRepository.save(fechamentoConta);
        pedidoRepository.save(pedido);

        return FechamentoContaResponse.fromEntiy(fechamento);
    }

    public FechamentoContaResponse buscarPorPedido(Long pedidoId) {
        FechamentoConta fechamentoConta = fechamentoRepository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new RegraNegocioException("Fechamento não encontrado"));
        return FechamentoContaResponse.fromEntiy(fechamentoConta);
    }

    private Pedido buscarPedidoPorId(Long pedidoId) {
        return pedidoRepository.findById(pedidoId).orElseThrow(
                () -> new RegraNegocioException("Pedido não encontrado")
        );
    }
}
