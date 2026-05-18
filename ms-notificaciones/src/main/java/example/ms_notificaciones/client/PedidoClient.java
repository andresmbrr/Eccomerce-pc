package example.ms_notificaciones.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "ms-pedidos")
public interface PedidoClient {

}