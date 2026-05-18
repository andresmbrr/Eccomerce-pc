package example.ms_stock.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "ms-producto")
public interface ProductClient {

}