package example.ms_pagos.client;


import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "ms-pedidos")
public interface OrderClient {

}