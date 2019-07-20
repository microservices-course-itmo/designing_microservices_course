package com.microservices.tariffmanagement.service;

import com.microservices.tariffmanagement.dto.CreationTariffDto;
import com.microservices.tariffmanagement.dto.TariffDto;

import java.util.List;

public interface TariffService {
    TariffDto createTariff(CreationTariffDto tariff);

    List<TariffDto> getAllTariffs();

    TariffDto getTariffById(int tariffId);
}
