package example.ms_carrito.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "ms-producto")
public interface ProductClient {

}