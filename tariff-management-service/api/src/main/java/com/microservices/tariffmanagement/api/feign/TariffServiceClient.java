package com.microservices.tariffmanagement.api.feign;

import com.microservices.tariffmanagement.api.dto.TariffDto;
import com.microservices.tariffmanagement.api.service.TariffService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Primary
@FeignClient(value = "tariff-management-service/tariffs")
public interface TariffServiceClient extends TariffService {
    @GetMapping
    List<TariffDto> getTariffs();

    @GetMapping(value = "{tariffId}")
    TariffDto getTariffById(@PathVariable("tariffId") int id);
}
