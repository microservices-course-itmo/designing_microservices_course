package com.microservices.tariffmanagement.service;

import com.microservices.tariffmanagement.api.dto.TariffDto;
import com.microservices.tariffmanagement.dto.CreationTariffDto;

import java.util.List;

public interface TariffService {
    TariffDto createTariff(CreationTariffDto tariff);

    List<TariffDto> getAllTariffs();

    TariffDto getTariffById(int tariffId);

    void deleteTariffById(int tariffId);
}
