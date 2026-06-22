package br.com.losystem.chefdesk.client;

import br.com.losystem.chefdesk.dto.request.PagamentoRequest;
import br.com.losystem.chefdesk.dto.response.PagamentoResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "pagamento-client", url = "${pagamento.api.url}")
public interface PagamentoClient {

    @PostMapping("/pagamento/processar")
    PagamentoResponse processar(@RequestBody PagamentoRequest request);

}
