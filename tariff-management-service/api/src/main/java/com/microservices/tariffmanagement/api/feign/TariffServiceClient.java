package com.microservices.tariffmanagement.api.feign;

import com.microservices.tariffmanagement.api.dto.TariffDto;
import com.microservices.tariffmanagement.api.service.TariffServiceApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Defines the parameters and paths of REST API of tariff management api
 * <p>
 * Java feign client will be generated based on this declaration.
 * <p>
 * We use placeholder in the {@link FeignClient#name()} field in order
 * for Ribbon load balancing client and Eureka discovery service to understand
 * the name of the service the request should be redirected to.
 */
@FeignClient(name = "${tariff.management.service.name}/tariffs")
public interface TariffServiceClient extends TariffServiceApi {

    /**
     * {@inheritDoc}
     */
    @GetMapping
    List<TariffDto> getTariffs();

    /**
     * {@inheritDoc}
     */
    @GetMapping(value = "{tariffId}")
    TariffDto getTariffById(@PathVariable("tariffId") int id);
}
