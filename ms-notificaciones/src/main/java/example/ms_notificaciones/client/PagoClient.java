package example.ms_notificaciones.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "ms-pagos")
public interface PagoClient {

}