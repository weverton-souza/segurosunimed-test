package com.example.api.client;

import com.example.api.to.ViaCepResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "viacep-api", url = "https://viacep.com.br/ws")
public interface ViaCepClient {

    @GetMapping("/{zipCode}/json")
    ViaCepResponse findAddressByCep(@PathVariable String zipCode);
}
