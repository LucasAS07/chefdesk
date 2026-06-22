package br.com.losystem.chefdesk.service;

import br.com.losystem.chefdesk.client.PagamentoClient;
import br.com.losystem.chefdesk.domain.entity.FechamentoConta;
import br.com.losystem.chefdesk.domain.entity.Mesa;
import br.com.losystem.chefdesk.domain.entity.Pagamento;
import br.com.losystem.chefdesk.domain.entity.Pedido;
import br.com.losystem.chefdesk.domain.enums.FormaPagamento;
import br.com.losystem.chefdesk.domain.enums.StatusMesa;
import br.com.losystem.chefdesk.domain.enums.StatusPagamento;
import br.com.losystem.chefdesk.domain.enums.StatusPedido;
import br.com.losystem.chefdesk.dto.request.PagamentoRequest;
import br.com.losystem.chefdesk.dto.response.PagamentoResponse;
import br.com.losystem.chefdesk.exception.RegraNegocioException;
import br.com.losystem.chefdesk.repository.FechamentoContaRepository;
import br.com.losystem.chefdesk.repository.MesaRepository;
import br.com.losystem.chefdesk.repository.PagamentoRepository;
import br.com.losystem.chefdesk.repository.PedidoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PagamentoService {

    private final PagamentoClient pagamentoClient;
    private final FechamentoContaRepository fechamentoRepository;
    private final PedidoRepository pedidoRepository;
    private final MesaRepository mesaRepository;
    private final PagamentoRepository pagamentoRepository;

    public PagamentoService(PagamentoClient pagamentoClient, FechamentoContaRepository fechamentoRepository,
                            PedidoRepository pedidoRepository, MesaRepository mesaRepository,
                            PagamentoRepository pagamentoRepository) {
        this.pagamentoClient = pagamentoClient;
        this.fechamentoRepository = fechamentoRepository;
        this.pedidoRepository = pedidoRepository;
        this.mesaRepository = mesaRepository;
        this.pagamentoRepository = pagamentoRepository;
    }

    @Transactional
    public void pagar(Long pedidoId, String formaPagamento) {
        FechamentoConta fechamentoConta = fechamentoRepository.findByPedidoId(pedidoId).orElseThrow(
                () -> new RegraNegocioException("Conta não encontrada")
        );

        PagamentoResponse response = pagamentoClient.processar(
                new PagamentoRequest(
                        fechamentoConta.getTotal(),
                        formaPagamento
                )
        );

        if ("APROVADO".equals(response.status())) {
            Pedido pedido = fechamentoConta.getPedido();
            pedido.setStatus(StatusPedido.FECHADO);

            Mesa mesa = pedido.getMesa();
            mesa.setStatus(StatusMesa.LIVRE);

            Pagamento pagamento = new Pagamento();
            pagamento.setPedido(pedido);
            pagamento.setFormaPagamento(FormaPagamento.valueOf(formaPagamento));
            pagamento.setStatus(StatusPagamento.APROVADO);
            pagamento.setValor(fechamentoConta.getTotal());
            pagamento.setDataPagamento(fechamentoConta.getDataFechamento());

            pedidoRepository.save(pedido);
            mesaRepository.save(mesa);
            pagamentoRepository.save(pagamento);
        }
    }

}
