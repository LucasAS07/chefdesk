package br.com.losystem.chefdesk.worker;

import br.com.losystem.chefdesk.domain.entity.PedidoItem;
import br.com.losystem.chefdesk.domain.enums.StatusItemPedido;
import br.com.losystem.chefdesk.repository.PedidoItemRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class CozinhaWorker {

    private final PedidoItemRepository itemRepository;

    private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    public CozinhaWorker(PedidoItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void verificarItensAtrasados() {
        List<PedidoItem> itensEmPreparo = itemRepository.buscarItensComProdutoEPedido(StatusItemPedido.EM_PREPARO);
        for (PedidoItem pedidoItem : itensEmPreparo) {
            executorService.submit(() -> verificarItem(pedidoItem));
        }
    }

    private void verificarItem(PedidoItem pedidoItem) {
        if (pedidoItem.getDataInicioPreparo() == null) {
            return;
        }

        Integer tempoPreparo = pedidoItem.getProduto().getTempoPreparoMinutos();
        if (tempoPreparo == null || tempoPreparo <= 0) {
            return;
        }

        long minutosEmPreparo = Duration.between(pedidoItem.getDataInicioPreparo(), LocalDateTime.now()).toMinutes();
        if (minutosEmPreparo > tempoPreparo) {
            System.out.println(
                    """
                    [ALERTA COZINHA]
                    Item atrassado:
                    Pedido: %d
                    Mesa: %d
                    Produto: %s
                    Tempo esperado: %d minutos
                    Tempo em Preparo: %d minutos
                    """.formatted(
                            pedidoItem.getPedido().getId(),
                            pedidoItem.getPedido().getMesa().getNumero(),
                            pedidoItem.getProduto().getNome(),
                            tempoPreparo,
                            minutosEmPreparo
                    )

            );
        }
    }
}
