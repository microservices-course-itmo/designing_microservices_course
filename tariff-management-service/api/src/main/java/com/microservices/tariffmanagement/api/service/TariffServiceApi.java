package com.microservices.tariffmanagement.api.service;

import com.microservices.tariffmanagement.api.dto.TariffDto;

import java.util.List;

/**
 * Defines interface for communication with User-management-service
 */
public interface TariffServiceApi {
    /**
     * Getting all available tariffs
     */
    List<TariffDto> getTariffs();

    /**
     * Getting tariff by id
     */
    TariffDto getTariffById(int id);
}
